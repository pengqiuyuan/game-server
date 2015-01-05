function Custom(){
	
	//设置cookie
	this.setCookie=function(c_name,value,expiredays){
		var exdate=new Date();
		exdate.setDate(exdate.getDate()+expiredays);
		document.cookie=c_name+ "=" +escape(value)+
		((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
	};
	//取cookie
	this.getCookie=function(c_name)
	{
	if (document.cookie.length>0)
	  {
	  c_start=document.cookie.indexOf(c_name + "=");
	  if (c_start!=-1)
	    { 
	    c_start=c_start + c_name.length+1 ;
	    c_end=document.cookie.indexOf(";",c_start);
	    if (c_end==-1) c_end=document.cookie.length;
	    return unescape(document.cookie.substring(c_start,c_end));
	    } 
	  }
	return "";
	};
}

(function($){ 
	$.fn.serializeJson=function(){ 
	var serializeObj={}; 
	var array=this.serializeArray(); 
	var str=this.serialize(); 
	$(array).each(function(){ 
	if(serializeObj[this.name]){ 
	if($.isArray(serializeObj[this.name])){ 
	serializeObj[this.name].push(this.value); 
	}else{ 
	serializeObj[this.name]=[serializeObj[this.name],this.value]; 
	} 
	}else{ 
	serializeObj[this.name]=this.value; 
	} 
	}); 
	return serializeObj; 
	}; 
	})(jQuery); 