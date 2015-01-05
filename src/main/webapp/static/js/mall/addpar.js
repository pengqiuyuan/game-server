
		$(function(){
			
			var col="";//用于记录当前选中的颜色
			var siz="";//用于记录当前选中的尺码
			var cost = $("#cost").val();//默认价格
			var commodityId = $("#id").val();//商品id
			
			$("#all").click(function (){
				$("#color :checkbox").attr("checked", !!$("#all").attr("checked"));
				if($("#all").attr("checked")){add();}
			});
			$("#size :checkbox").click(function (){if(this.checked)add();else removesize(this.value);});
			$("#color :checkbox").click(function (){if(this.checked)add();else removecolor(this.value);});
		
			$("input[id^='size']").keyup(function(){changesize($(this).attr("id"),this.value);});
			$("input[id^='col']").keyup(function(){changecolor($(this).attr("id"),this.value);});
			
			function changesize(id,value){
				var s = $('span[name="'+id+'"]');
				siz = siz.replace(s.html(), value);
				for(var i = 0 ; i < s.length ;i++){
					$($(s[i]).find("input")).val(value);
				}
			}
			
			function changecolor(id,value){
				var s = $('td[name="'+id+'"]');
				col=col.replace(s.html(), value);
				s.html(value);
			}
			
			function removecolor(color){
				var s = $('td[name="'+color+'"]');
				col=col.replace($(s[0]).html(),"");
				$("."+color).parent().remove();
			}
			
			function removesize(size){
				var s = $('span[name="'+size+'"]');
				siz=siz.replace($($(s[0]).find("input")).val(),"");
				$("."+size).parent().remove();
				if($("#size  input:checked").length==0){
					col="";
					$(".col").remove();
				}
			}
			
			function add(){
				var colors = $("#color  input:checked");
				var sizes = $("#size  input:checked");
				var tr = "";
				if(colors.length>0&&sizes.length>0){
					var newcolor = "";
					for(var i = 0 ; i < colors.length; i++){
						if(col.indexOf($("#"+colors[i].value).val())<0){
							newcolor=colors[i].value;
							tr=addtr($("#"+colors[i].value).val(),colors[i].value);
							col+=$("#"+colors[i].value).val();
							$("#par").append(tr);
						}
					}
					tr="";
					for(var i = 0 ; i<sizes.length;i++){
						if(siz.indexOf($("#"+sizes[i].value).val())<0){
							tr=addsp($("#"+sizes[i].value).val(),sizes[i].value);
							siz+=$("#"+sizes[i].value).val();
							$("td[class^=siz]").append(tr);
						}else{
							if(newcolor!=""){
								tr=addsp($("#"+sizes[i].value).val(),sizes[i].value);
								$(".siz"+newcolor).append(tr);
							}
						}
					}
				}
			}
			function addtr(color,col){
				return '<tr class="col"><td width="70" class="'+col+'" name="'+col+'">'+color+'</td><td class="siz'+col+'"></td></tr>';
			}
			function addsp(size,clas){
				return '<div class="sizediv" ref="'+size+'">'
				+'<span style="width:160px;float:left;text-align:center;" class="'+clas+'" name="'+clas+'"><input type="text" style="width:120px; height:10px;font-size:10px; height:15px;" value="'+size+'"  readonly="readonly"/></span>'
				+'<span><input type="text" name="price'+clas+'" value="'+cost+'" style="width:50px; height:10px;font-size:10px; height:15px;" onkeyup="if(isNaN(value))execCommand(\'undo\')" onafterpaste="if(isNaN(value))execCommand(\'undo\')"/></span>'
				+'<span style="padding-left:60px;">'
				+'<input name="count'+clas+'" type="text" style="width:50px; font-size:10px; height:15px;" onkeyup="this.value=this.value.replace(/\\D/g,\'\');" onafterpaste="this.value=this.value.replace(/\\D/g,\'\')"/></span>'
				+'</div>';
			}
			
			

			
			
			//发送json
			$("a[class='btn']").click(function (){
				var color = $("tr=[class='col']");
				var json = '[';
				for(var i = 0 ; i<color.length ;i++){
					var siz = $(color[i]).find("td");
					json+=getSizRs($(siz[0]),$(siz[1]));
				}
				json = json.substring(0, json.length-1)+"]";
				
				$.ajax({
					url: "/mgr/mall/commodity/par/save?json="+json,
					type: 'POST',
					contentType: "application/json;charset=UTF-8",
					success: function(data){
						if(data=="ok") location.replace(location.href);
					},error:function(xhr){
						alert('错误了，请重试');
					}
				});
			});
			
			
			function getSizRs(colour,siz){
				var div = $(siz).find("div");
				var json="";
				for(var i = 0 ; i < div.length ;i++){
					var input = $($(div[i]).find("input"));
					if(input[2].value=="")input[2].value=0;
					json+='{"commodityId":'+commodityId+',"colour":"'+colour.html()+'","size":"'+input[0].value+'","price":'+input[1].value+',"count":'+input[2].value+'},';
				}
				return json;
			}
			
			
			
			
			
		});
		
		
		
		
		