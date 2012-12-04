<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'good.show.css')}" />
</head>
<body>
<g:render template="summary"/>
<h1>Positivas</h1>
<ul class="green">
	<% news.positives.each { item -> %>
	<li>
		<h2><a href="${item.link}" target="_blank">${item.title}</a></h2>
		<h3>${item.feed}: ${item.pubDate} [${item.weight}]</h3>
		<div>${item.description}</div>
	</li>
	<%}%>
</ul>
<h1>Neutrales</h1>
<ul class="yellow">
	<% news.neutral.each { item -> %>
	<li>
		<h2><a href="${item.link}" target="_blank">${item.title}</a></h2>
		<h3>${item.feed}: ${item.pubDate} [${item.weight}]</h3>
		<div>${item.description}</div>
	</li>
	<%}%>
</ul>
<h1>Negativas</h1>
<ul class="red">
	<% news.negatives.each { item -> %>
	<li>
		<h2><a href="${item.link}" target="_blank">${item.title}</a></h2>
		<h3>${item.feed}: ${item.pubDate} [${item.weight}]</h3>
		<div>${item.description}</div>
	</li>
	<%}%>
</ul>
</body>
</html>