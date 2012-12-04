package good.show

import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors

import org.apache.commons.lang.StringUtils


class GoodShowController {

	def MAX_THREADS = 10
	def THRESHOLD = 1
	def grailsApplication, positives, negatives, model
	def rss = [
		[feed: 'Infobae', url:'http://www.infobae.com/rss/hoy.xml'],
		[feed: 'La Nación', url:'http://contenidos.lanacion.com.ar/herramientas/rss/origen=2'],
		[feed: 'Clarín', url:'http://www.clarin.com/rss/'],
		[feed: 'El Observador', url:'http://www.elobservador.com.uy/rss/portada/'],
		[feed: 'El País', url:'http://elpaisweb5.elpais.com.uy/formatos/rss/index.asp?seccion=umomento'],
		[feed: 'Espectador', url:'http://www.espectador.com/xrss/rss.php?m=z&z=0'],
		[feed: 'Perfil', url:'http://www.perfil.com/rss/ultimomomento.xml'],
		[feed: 'Página 12', url:'http://www.pagina12.com.ar/diario/rss/ultimas_noticias.xml'],
		[feed: 'El Sensacional', url:'http://www.elsensacional.com/files/rss/ultimasnoticias.xml']
//		,
//		'http://www.cronica.com.ar/rss/informacion-general.xml'
	]

	def index() {
		def df = new SimpleDateFormat('[yyyy/MM/dd] HH.mm.ss')
		println "Start: ${df.format(new Date())}"
		String.metaClass.fileAsArray = {
			grailsAttributes.getApplicationContext().getResource(delegate).getFile().getText().split(/\s\s/)
		}

		model = []

		positives = grailsApplication.config.dictionary.positives.path.fileAsArray()
		negatives = grailsApplication.config.dictionary.negatives.path.fileAsArray()

		def pool = Executors.newFixedThreadPool(MAX_THREADS)
		def ecs = new ExecutorCompletionService<Void>(pool);

		rss.each {
			ecs.submit(new fetch(it), Void)
		}
		rss.each {
			ecs.take().get()
		}
		pool.shutdown()

		def news = [positives:[], neutral:[], negatives:[]]
		model.each {
			if (it.weight < -THRESHOLD)
				news.negatives.add(it)
			else if (it.weight > THRESHOLD)
				news.positives.add(it)
			else
				news.neutral.add(it)
		}
		news.negatives.sort {x,y -> x.weight <=> y.weight ?: x.title <=> y.title}
		news.positives.sort {x,y -> y.weight <=> x.weight ?: x.title <=> y.title}
		println "End: ${df.format(new Date())}"
		render(template:'news', model:[news:news])
	}

	class fetch implements Runnable {
		LinkedHashMap rss
		fetch(LinkedHashMap newRss) {
			this.rss = newRss
		}
		public void run() {
			println "Fetching ${rss.feed}: ${rss.url} ..."
			def feedName = rss.feed
			def feeds = new XmlSlurper().parse(rss.url)
			feeds.channel.childNodes().each {
				if (it.name() == 'item') {
					def item = [:]
					it.childNodes().each {
						if (it.name() in ['title', 'description', 'link', 'pubDate'] && !StringUtils.isBlank(it.text())) {
							if (it.name() == 'title') {
								item.title = it.text()
							} else if(it.name() == 'description') {
								item.description = it.text().replaceAll(/<.*>/, '')
							} else if(it.name() == 'link') {
								item.link = it.text()
							} else if(it.name() == 'pubDate') {
								item.pubDate = it.text()
							}
						}
					}
					item.words = [] as Set
					item.weight = 0
					item.feed = feedName
					def pattern = ~/(\s*[a-zA-ZÃ¡Ã�Ã©Ã‰Ã­Ã�Ã³Ã“ÃºÃšÃ¤Ã„Ã«Ã‰Ã¯Ã�Ã¶Ã–Ã¼ÃœÃ Ã€Ã¨ÃˆÃ¬ÃŒÃ²Ã’Ã¹Ã™Ã±Ã‘Ã§Ã¦Ã‡Ã†Ã£ÃƒÃ•Ãµ][a-zA-ZÃ¡Ã�Ã©Ã‰Ã­Ã�Ã³Ã“ÃºÃšÃ¤Ã„Ã«Ã‰Ã¯Ã�Ã¶Ã–Ã¼ÃœÃ Ã€Ã¨ÃˆÃ¬ÃŒÃ²Ã’Ã¹Ã™Ã±Ã‘Ã§Ã¦Ã‡Ã†Ã£ÃƒÃ•Ãµ_1234567890]*\s*)/
					def matcher = pattern.matcher((item.title?:'').concat(' ').concat(item.description?:''))
					for(i in 0..<matcher.getCount()) {
						String word = (matcher[i][0]).trim().toLowerCase()
						item.words.add(word)
						positives.each {
							if (StringUtils.startsWith(word, it.trim())) {
								item.weight++
							}
						}
						negatives.each {
							if (StringUtils.startsWith(word, it.trim())) {
								item.weight--
							}
						}
					}
					model.add(item)
				}
			}
		}
	}

}