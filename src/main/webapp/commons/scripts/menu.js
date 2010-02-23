	var timeout = 500;
	var closetimer = 0;
	var ddmenuitem = 0;

	function jsddm_open()
	{
		jsddm_canceltimer();
		jsddm_close();
		ddmenuitem = $(this).find('ul').eq(0).css('visibility', 'visible').css('zindex','99999');
	}

	function jsddm_close()
	{
		if (ddmenuitem)
			ddmenuitem.css('visibility', 'hidden');
	}

	function jsddm_timer()
	{
		closetimer = window.setTimeout(jsddm_close, timeout);
	}

	function jsddm_canceltimer()
	{
		if (closetimer)
		{
			window.clearTimeout(closetimer);
			closetimer = null;
		}
	}

	$(document).ready( function()
	{
		var $jsddmLis = $('#jsddm > li'); 
		$jsddmLis.bind('mouseover', jsddm_open);
		$jsddmLis.bind('mouseout', jsddm_timer);
	});

	document.onclick = jsddm_close;
	
