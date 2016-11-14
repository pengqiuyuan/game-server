<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
	
<script type="text/javascript">
    require.config({
        paths: {
            echarts: '../../../static/echarts/js'
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/bar',
            'echarts/chart/line',
            'echarts/chart/map'
        ],
        function (ec) {
            var nextName = [];
            var nextDate =[];
            var nextSeries = [];
        	var nd = 0;
            for(var key in ${next}){
            	nextName.push(key);
            	var da = [];
            	for ( var datakey in ${next}[key]) {  
            		if(nd == 0){
            			nextDate.push(datakey)
            		}
            		da.push(${next}[key][datakey])                    
                }  
            	nextSeries.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var sevenName = [];
            var sevenDate = [];
            var sevenSeries = [];
            var sd = 0;
            for(var key in ${seven}){
            	sevenName.push(key);
            	var da = [];
            	for ( var datakey in ${seven}[key]) {  
            		if(sd == 0){
            			sevenDate.push(datakey)
            		}
            		da.push(${seven}[key][datakey])                   
                } 
            	sevenSeries.push({name:key,type:'line',data:da});
            	sd = 1;
            }
            
            var thirtyName = [];
            var thirtyDate = [];
            var thirtySeries = [];
            var td = 0;
            for(var key in ${thirty}){
            	thirtyName.push(key);
            	var da = [];
            	for ( var datakey in ${thirty}[key]) {  
            		if(td == 0){
            			thirtyDate.push(datakey)
            		}
            		da.push(${thirty}[key][datakey])                   
                } 
            	thirtySeries.push({name:key,type:'line',data:da});
            	td = 1;
            }
            
            var d2Name = [];
            var d2Date =[];
            var d2Series = [];
        	var nd = 0;
            for(var key in ${d2}){
            	d2Name.push(key);
            	var da = [];
            	for ( var datakey in ${d2}[key]) {  
            		if(nd == 0){
            			d2Date.push(datakey)
            		}
            		da.push(${d2}[key][datakey])                    
                }  
            	d2Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d3Name = [];
            var d3Date =[];
            var d3Series = [];
        	var nd = 0;
            for(var key in ${d3}){
            	d3Name.push(key);
            	var da = [];
            	for ( var datakey in ${d3}[key]) {  
            		if(nd == 0){
            			d3Date.push(datakey)
            		}
            		da.push(${d3}[key][datakey])                    
                }  
            	d3Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d4Name = [];
            var d4Date =[];
            var d4Series = [];
        	var nd = 0;
            for(var key in ${d4}){
            	d4Name.push(key);
            	var da = [];
            	for ( var datakey in ${d4}[key]) {  
            		if(nd == 0){
            			d4Date.push(datakey)
            		}
            		da.push(${d4}[key][datakey])                    
                }  
            	d4Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d5Name = [];
            var d5Date =[];
            var d5Series = [];
        	var nd = 0;
            for(var key in ${d5}){
            	d5Name.push(key);
            	var da = [];
            	for ( var datakey in ${d5}[key]) {  
            		if(nd == 0){
            			d5Date.push(datakey)
            		}
            		da.push(${d5}[key][datakey])                    
                }  
            	d5Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d6Name = [];
            var d6Date =[];
            var d6Series = [];
        	var nd = 0;
            for(var key in ${d6}){
            	d6Name.push(key);
            	var da = [];
            	for ( var datakey in ${d6}[key]) {  
            		if(nd == 0){
            			d6Date.push(datakey)
            		}
            		da.push(${d6}[key][datakey])                    
                }  
            	d6Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d8Name = [];
            var d8Date =[];
            var d8Series = [];
        	var nd = 0;
            for(var key in ${d8}){
            	d8Name.push(key);
            	var da = [];
            	for ( var datakey in ${d8}[key]) {  
            		if(nd == 0){
            			d8Date.push(datakey)
            		}
            		da.push(${d8}[key][datakey])                    
                }  
            	d8Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d9Name = [];
            var d9Date =[];
            var d9Series = [];
        	var nd = 0;
            for(var key in ${d9}){
            	d9Name.push(key);
            	var da = [];
            	for ( var datakey in ${d9}[key]) {  
            		if(nd == 0){
            			d9Date.push(datakey)
            		}
            		da.push(${d9}[key][datakey])                    
                }  
            	d9Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d10Name = [];
            var d10Date =[];
            var d10Series = [];
        	var nd = 0;
            for(var key in ${d10}){
            	d10Name.push(key);
            	var da = [];
            	for ( var datakey in ${d10}[key]) {  
            		if(nd == 0){
            			d10Date.push(datakey)
            		}
            		da.push(${d10}[key][datakey])                    
                }  
            	d10Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d11Name = [];
            var d11Date =[];
            var d11Series = [];
        	var nd = 0;
            for(var key in ${d11}){
            	d11Name.push(key);
            	var da = [];
            	for ( var datakey in ${d11}[key]) {  
            		if(nd == 0){
            			d11Date.push(datakey)
            		}
            		da.push(${d11}[key][datakey])                    
                }  
            	d11Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d12Name = [];
            var d12Date =[];
            var d12Series = [];
        	var nd = 0;
            for(var key in ${d12}){
            	d12Name.push(key);
            	var da = [];
            	for ( var datakey in ${d12}[key]) {  
            		if(nd == 0){
            			d12Date.push(datakey)
            		}
            		da.push(${d12}[key][datakey])                    
                }  
            	d12Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d13Name = [];
            var d13Date =[];
            var d13Series = [];
        	var nd = 0;
            for(var key in ${d13}){
            	d13Name.push(key);
            	var da = [];
            	for ( var datakey in ${d13}[key]) {  
            		if(nd == 0){
            			d13Date.push(datakey)
            		}
            		da.push(${d13}[key][datakey])                    
                }  
            	d13Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d14Name = [];
            var d14Date =[];
            var d14Series = [];
        	var nd = 0;
            for(var key in ${d14}){
            	d14Name.push(key);
            	var da = [];
            	for ( var datakey in ${d14}[key]) {  
            		if(nd == 0){
            			d14Date.push(datakey)
            		}
            		da.push(${d14}[key][datakey])                    
                }  
            	d14Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d15Name = [];
            var d15Date =[];
            var d15Series = [];
        	var nd = 0;
            for(var key in ${d15}){
            	d15Name.push(key);
            	var da = [];
            	for ( var datakey in ${d15}[key]) {  
            		if(nd == 0){
            			d15Date.push(datakey)
            		}
            		da.push(${d15}[key][datakey])                    
                }  
            	d15Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d16Name = [];
            var d16Date =[];
            var d16Series = [];
        	var nd = 0;
            for(var key in ${d16}){
            	d16Name.push(key);
            	var da = [];
            	for ( var datakey in ${d16}[key]) {  
            		if(nd == 0){
            			d16Date.push(datakey)
            		}
            		da.push(${d16}[key][datakey])                    
                }  
            	d16Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d17Name = [];
            var d17Date =[];
            var d17Series = [];
        	var nd = 0;
            for(var key in ${d17}){
            	d17Name.push(key);
            	var da = [];
            	for ( var datakey in ${d17}[key]) {  
            		if(nd == 0){
            			d17Date.push(datakey)
            		}
            		da.push(${d17}[key][datakey])                    
                }  
            	d17Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d18Name = [];
            var d18Date =[];
            var d18Series = [];
        	var nd = 0;
            for(var key in ${d18}){
            	d18Name.push(key);
            	var da = [];
            	for ( var datakey in ${d18}[key]) {  
            		if(nd == 0){
            			d18Date.push(datakey)
            		}
            		da.push(${d18}[key][datakey])                    
                }  
            	d18Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d19Name = [];
            var d19Date =[];
            var d19Series = [];
        	var nd = 0;
            for(var key in ${d19}){
            	d19Name.push(key);
            	var da = [];
            	for ( var datakey in ${d19}[key]) {  
            		if(nd == 0){
            			d19Date.push(datakey)
            		}
            		da.push(${d19}[key][datakey])                    
                }  
            	d19Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d20Name = [];
            var d20Date =[];
            var d20Series = [];
        	var nd = 0;
            for(var key in ${d20}){
            	d20Name.push(key);
            	var da = [];
            	for ( var datakey in ${d20}[key]) {  
            		if(nd == 0){
            			d20Date.push(datakey)
            		}
            		da.push(${d20}[key][datakey])                    
                }  
            	d20Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d21Name = [];
            var d21Date =[];
            var d21Series = [];
        	var nd = 0;
            for(var key in ${d21}){
            	d21Name.push(key);
            	var da = [];
            	for ( var datakey in ${d21}[key]) {  
            		if(nd == 0){
            			d21Date.push(datakey)
            		}
            		da.push(${d21}[key][datakey])                    
                }  
            	d21Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d22Name = [];
            var d22Date =[];
            var d22Series = [];
        	var nd = 0;
            for(var key in ${d22}){
            	d22Name.push(key);
            	var da = [];
            	for ( var datakey in ${d22}[key]) {  
            		if(nd == 0){
            			d22Date.push(datakey)
            		}
            		da.push(${d22}[key][datakey])                    
                }  
            	d22Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d23Name = [];
            var d23Date =[];
            var d23Series = [];
        	var nd = 0;
            for(var key in ${d23}){
            	d23Name.push(key);
            	var da = [];
            	for ( var datakey in ${d23}[key]) {  
            		if(nd == 0){
            			d23Date.push(datakey)
            		}
            		da.push(${d23}[key][datakey])                    
                }  
            	d23Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d24Name = [];
            var d24Date =[];
            var d24Series = [];
        	var nd = 0;
            for(var key in ${d24}){
            	d24Name.push(key);
            	var da = [];
            	for ( var datakey in ${d24}[key]) {  
            		if(nd == 0){
            			d24Date.push(datakey)
            		}
            		da.push(${d24}[key][datakey])                    
                }  
            	d24Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d25Name = [];
            var d25Date =[];
            var d25Series = [];
        	var nd = 0;
            for(var key in ${d25}){
            	d25Name.push(key);
            	var da = [];
            	for ( var datakey in ${d25}[key]) {  
            		if(nd == 0){
            			d25Date.push(datakey)
            		}
            		da.push(${d25}[key][datakey])                    
                }  
            	d25Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d26Name = [];
            var d26Date =[];
            var d26Series = [];
        	var nd = 0;
            for(var key in ${d26}){
            	d26Name.push(key);
            	var da = [];
            	for ( var datakey in ${d26}[key]) {  
            		if(nd == 0){
            			d26Date.push(datakey)
            		}
            		da.push(${d26}[key][datakey])                    
                }  
            	d26Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d27Name = [];
            var d27Date =[];
            var d27Series = [];
        	var nd = 0;
            for(var key in ${d27}){
            	d27Name.push(key);
            	var da = [];
            	for ( var datakey in ${d27}[key]) {  
            		if(nd == 0){
            			d27Date.push(datakey)
            		}
            		da.push(${d27}[key][datakey])                    
                }  
            	d27Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d28Name = [];
            var d28Date =[];
            var d28Series = [];
        	var nd = 0;
            for(var key in ${d28}){
            	d28Name.push(key);
            	var da = [];
            	for ( var datakey in ${d28}[key]) {  
            		if(nd == 0){
            			d28Date.push(datakey)
            		}
            		da.push(${d28}[key][datakey])                    
                }  
            	d28Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            var d29Name = [];
            var d29Date =[];
            var d29Series = [];
        	var nd = 0;
            for(var key in ${d29}){
            	d29Name.push(key);
            	var da = [];
            	for ( var datakey in ${d29}[key]) {  
            		if(nd == 0){
            			d29Date.push(datakey)
            		}
            		da.push(${d29}[key][datakey])                    
                }  
            	d29Series.push({name:key,type:'line',data:da});
            	nd = 1;
            }
            
            //--- 折柱 ---
            var myChart = ec.init(document.getElementById('mainNextDay'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '次日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在5月4日这一天内还有玩过游戏，5月3日的次日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:nextName
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : nextDate
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : nextSeries
            });
            
            var myChart = ec.init(document.getElementById('mainSevenDay'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '七日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在5月10日这一天内还有玩过游戏，5月3日的7日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:sevenName
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : sevenDate
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : sevenSeries
            });
            
            var myChart = ec.init(document.getElementById('mainThirtyDay'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '三十日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:thirtyName
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : thirtyDate
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : thirtySeries
            });
            
           
            var myChart = ec.init(document.getElementById('maind2Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '2日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d2Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d2Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d2Series
            });
            
            
            var myChart = ec.init(document.getElementById('maind3Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '3日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d3Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d3Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d3Series
            });
            
            var myChart = ec.init(document.getElementById('maind4Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '4日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d4Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d4Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d4Series
            });
            
            var myChart = ec.init(document.getElementById('maind5Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '5日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d5Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d5Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d5Series
            });
            
            var myChart = ec.init(document.getElementById('maind6Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '6日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d6Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d6Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d6Series
            });
            
            var myChart = ec.init(document.getElementById('maind8Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '8日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d8Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d8Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d8Series
            });
            
            var myChart = ec.init(document.getElementById('maind9Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '9日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d9Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d9Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d9Series
            });
            
            var myChart = ec.init(document.getElementById('maind10Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '10日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d10Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d10Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d10Series
            });
            
            var myChart = ec.init(document.getElementById('maind11Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '11日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d11Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d11Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d11Series
            });
            
            var myChart = ec.init(document.getElementById('maind12Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '12日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d12Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d12Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d12Series
            });
            
            var myChart = ec.init(document.getElementById('maind13Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '13日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d13Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d13Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d13Series
            });
            
            var myChart = ec.init(document.getElementById('maind14Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '14日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d14Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d14Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d14Series
            });
            
            var myChart = ec.init(document.getElementById('maind15Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '15日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d15Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d15Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d15Series
            });
            
            var myChart = ec.init(document.getElementById('maind16Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '16日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d16Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d16Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d16Series
            });
            
            var myChart = ec.init(document.getElementById('maind17Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '17日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d17Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d17Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d17Series
            });
            
            var myChart = ec.init(document.getElementById('maind18Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '18日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d18Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d18Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d18Series
            });
            
            var myChart = ec.init(document.getElementById('maind19Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '19日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d19Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d19Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d19Series
            });
            
            var myChart = ec.init(document.getElementById('maind20Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '20日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d20Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d20Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d20Series
            });
            
            var myChart = ec.init(document.getElementById('maind21Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '21日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d21Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d21Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d21Series
            });
            
            var myChart = ec.init(document.getElementById('maind22Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '22日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d22Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d22Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d22Series
            });
            
            var myChart = ec.init(document.getElementById('maind23Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '23日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d23Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d23Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d23Series
            });
            
            var myChart = ec.init(document.getElementById('maind24Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '24日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d24Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d24Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d24Series
            });
            
            var myChart = ec.init(document.getElementById('maind25Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '25日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d25Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d25Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d25Series
            });
            
            var myChart = ec.init(document.getElementById('maind26Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '26日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d26Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d26Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d26Series
            });
            
            var myChart = ec.init(document.getElementById('maind27Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '27日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d27Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d27Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d27Series
            });
            
            var myChart = ec.init(document.getElementById('maind28Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '28日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d28Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d28Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d28Series
            });
            
            var myChart = ec.init(document.getElementById('maind29Day'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '29日留存率',
                    subtext: '某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在6月2日这一天内还有玩过游戏，5月3日的30日留存率=24/100=24%'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:d29Name
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : d29Date
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}％'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : d29Series
            });
        }
    );
    </script>
