package good.show



import grails.test.mixin.*

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Feed)
class FeedTests {

	static def INFOBAE_HOY = 'InfoBAE-Hoy'
	static def INFOBAE_HOY_URL = 'hoy.xml'
	static def INFOBAE_MUNDO = 'InfoBAE-Mundo'
	static def INFOBAE_MUNDO_URL = 'mundo.xml'

	void setUp() {
		Feed.list()*.delete()
	}

	void testPersist() {
		new Feed(name: INFOBAE_HOY, url: INFOBAE_HOY_URL).save()
		new Feed(name: INFOBAE_MUNDO, url: INFOBAE_MUNDO_URL).save()
		assert 2 == Feed.count()
		def actualFeed = Feed.findByName(INFOBAE_HOY)
		assert actualFeed
		assert INFOBAE_HOY == actualFeed.name
	}

	void testToString() {
//		def feed = new Feed(name: INFOBAE_HOY, url: INFOBAE_HOY_URL)
//		assertToString(feed, "${INFOBAE_HOY}: ${INFOBAE_HOY_URL}")
	}
}
