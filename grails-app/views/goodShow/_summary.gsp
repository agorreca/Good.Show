<ul id="summary">
	<li class="dark green">posit : <g:formatNumber number="${news.positives.size}" type="number" minIntegerDigits="3" /></li>
	<li class="dark yellow">neutr : <g:formatNumber number="${news.neutral.size}" type="number" minIntegerDigits="3" /></li>
	<li class="dark red">negat : <g:formatNumber number="${news.negatives.size}" type="number" minIntegerDigits="3" /></li>
	<li>Total : <g:formatNumber number="${news.positives.size + news.neutral.size + news.negatives.size}" type="number" minIntegerDigits="3" /></li>
</ul>