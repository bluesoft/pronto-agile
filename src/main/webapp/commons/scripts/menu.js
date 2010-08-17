$(function(){
	$("#prontoMenu").buildMenu({
          //template:"yourMenuVoiceTemplate",
          additionalData:"",
          menuSelector:".menuContainer",
          menuWidth:150,
          openOnRight:false,
          containment:"window",
          iconPath:pronto.raiz+"commons/icons/",
          hasImages:true,
          fadeInTime:100,
          fadeOutTime:200,
          menuTop:0,
          menuLeft:0,
          submenuTop:0,
          submenuLeft:4,
          opacity:1,
          shadow:false,
          shadowColor:"black",
          shadowOpacity:.2,
          openOnClick:true,
          closeOnMouseOut:false,
          closeAfter:500,
          minZindex:"auto",
          hoverIntent:0, //if you use jquery.hoverIntent.js set this to time in milliseconds; 0= false;
          submenuHoverIntent:0 //if you use jquery.hoverIntent.js set this to time in milliseconds; 0= false;
      });
});

