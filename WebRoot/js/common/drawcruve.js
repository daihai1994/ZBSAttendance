
		var chart;
		var colors=['#00FF00','#FF0000','#0000FF','#FF00FF','#808000','#00FFFF','#800080','#008080','#800000','#000000'];	
		var hccolors=['#058DC7','#50B432','#ED561B','#DDDF00','#24CBE5','#64E572','#FF9655','#FFF263','#6AF9C4'];

		
		
		
		Highcharts.theme = {
			colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
			chart: {
				backgroundColor: {
					linearGradient: [0, 0, 500, 500],
					stops: [
						[0, 'rgb(255, 255, 255)'],
						[1, 'rgb(240, 240, 255)']
					]
				},
				borderWidth: 2,
				borderColor:'#4572A7',
				plotBackgroundColor: 'rgba(255, 255, 255, .9)',
			 // plotShadow: true,
				plotBorderWidth: 1
			},
			title: {
				style: {
					color: '#000',
					font: 'bold 16px "Trebuchet MS", Verdana, sans-serif'
				}
			},
			subtitle: {
				style: {
					color: '#666666',
					font: 'bold 12px "Trebuchet MS", Verdana, sans-serif'
				}
			},
			xAxis: {
				gridLineWidth: 0,
				lineColor: '#000',
				tickColor: '#000',
				labels: {
					style: {
						color: '#000',
						font: '11px Trebuchet MS, Verdana, sans-serif'
					}
				},
				title: {
					style: {
						color: '#333',
						fontWeight: 'bold',
						fontSize: '12px',
						fontFamily: 'Trebuchet MS, Verdana, sans-serif'
					}
				}
			},
			yAxis: {
				gridLineWidth: 0,
				minorTickInterval: 'auto',
				lineColor: '#000',
				lineWidth: 1,
				tickWidth: 1,
				tickColor: '#000',
				labels: {
					style: {
						color: '#000',
						font: '11px Trebuchet MS, Verdana, sans-serif'
					}
				},
				title: {
					style: {
						color: '#333',
						fontWeight: 'bold',
						fontSize: '12px',
						fontFamily: 'Trebuchet MS, Verdana, sans-serif'
					}
				}
			},
			legend: {
				itemStyle: {
					font: '9pt Trebuchet MS, Verdana, sans-serif',
					color: 'black'

				},
				itemHoverStyle: {
					color: '#039'
				},
				itemHiddenStyle: {
					color: 'gray'
				}
			},
			labels: {
				style: {
					color: '#99b'
				}
			}
		};
		
		var highchartsOptions = Highcharts.setOptions(Highcharts.theme);	//画图配置		
		
		function drowBar(cruveid,ChartData){
			chart = new Highcharts.Chart({
				chart: {
					renderTo: cruveid,
					type: 'column'//zoomType: 'x'
				},
				title: {
					text: ChartData.curDmaName+':流量累计漏损统计图'
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					gridLineWidth: 0,
					categories: ChartData.barChart.barAXAis
				},
				yAxis: [{
					//min: 0,
					title: {
						style: {
							color: '#ED561B'
						},
						gridLineWidth: 0,
						text: '累计漏失率：%'
					},
					labels: {
						style: {
							color: '#ED561B'
						},
						formatter: function() {
							return this.value +'%';
						}
					}
					
				},{
					//min: 0,
					opposite: true,
					title: {
						text: '累计流量：m³'
					},
					labels: {
						style: {
							color: '#058DC7'
						},
						formatter: function() {
							return this.value +'m³';
						}
					}
				}],
				tooltip: {
					headerFormat: '<span style="font-size:10px;width:100px">时间:{point.key}</span><table style="font-size:10px;width:100px">',
					pointFormat: '<tr><td style="color:{series.color};padding:0;font-size:10px">{series.name}: </td><td style="padding:0;font-size:10px"><b>{point.y:.1f} </b></td></tr>',
					footerFormat: '</table>',
					shared: true,
					useHTML: true
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				series: [ {
					name: '漏失率(%)',
					color:  '#ED561B',
					data: ChartData.barChart.TotalflowRateData//[8.9, 3.8, 3.3, 1.4, 4.0, 4.3, 5.0, 5.6, 2.4, 5.2, 5.3, 1.2,8.9, 3.8, 3.3, 1.4, 4.0, 4.3, 5.0, 5.6, 2.4, 5.2, 5.3, 1.2]

				},{
					name: '输入(m³)',
					color:  '#058DC7',
					yAxis: 1,
					data: ChartData.barChart.inflowData//[49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]

				}, {
					name: '输出(m³)',
					color:  '#50B432',
					yAxis: 1,
					data: ChartData.barChart.outflowData//[83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]

				}]
			});
			
		}
		
		///**************************画曲线***********************************
		function drowCruve(cruveid,ChartData){
			chart = new Highcharts.Chart({
				chart: {
					renderTo: cruveid,
					zoomType: 'x'
				},
				title: {
					text:ChartData.curDmaName+":"+(cruveid==="nchartdiv1"?(ChartData.startDatetime1+" ~ "+ChartData.stopDatetime1):(ChartData.startDatetime+" ~ "+ChartData.stopDatetime))//ChartData.inflowRate.title
				},
				subtitle: {
					text:null
				},
				xAxis:[{
					type: 'datetime',
					max:ChartData.inflowRate.xAxisMax,
					title: {
						text: null
					},
					gridLineWidth: 0,
					labels: {
						formatter: function() {								   
							return  Highcharts.dateFormat('%m月%d日 %H:%M', this.value);
						}
					}
				}],
				yAxis: [{
					title: {
						text: '实时漏失率：%',
						style: {
							color:  '#ED561B'
						}
					},
					gridLineWidth: 0,
					labels: {
						style: {
							color: '#ED561B'
						},
						formatter: function() {
							return this.value +'%';
						}
					},
					//min:0,// 0.6,
				},{
					title: {
						text: '实时流量：m³/h',
						style: {
							color: '#058DC7'
						}
					},
					labels: {
						formatter: function() {
							return this.value +'m³/h';
						},
						style: {
							color: '#058DC7'
						}
					},
					reversed: false ,
					//min:0,// 0.6,
					opposite: true
				}],
				tooltip: {
					enabled:true,
					formatter: function() {
						return  (this.series.name == '漏失率(%)' ? '实时漏失率：'+this.y.toFixed(1)+'%' : (this.series.name == '输入水量(m³/h)' ? '实时输入水量：' : '实时输出水量：')+this.y.toFixed(1)+'m³/h')+'<br/>时间:'+Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x);
					}
				},
				exporting :{
					enabled:false
				},
				credits:{
					enabled:false
				},
				legend: {
					enabled: true
				},
				plotOptions: {
					areaspline: {
						fillColor: {
							linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
							stops: [
								[0, Highcharts.getOptions().colors[0]],
								[1, 'rgba(2,0,0,0)']
							]
						},
						lineWidth: 1,
						marker: {
							enabled: false,
							states: {
								hover: {
									enabled: true,
									radius: 5
								}
							}
						},
						shadow: false,
						states: {
							hover: {
								lineWidth: 1
							}
						}
					},
					spline: {						
						lineWidth: 3,
						marker: {
							enabled: false,
							states: {
								hover: {
									enabled: true,
									radius: 5
								}
							}
						},
						shadow: false,
						states: {
							hover: {
								lineWidth: 3
							}
						}
					},
					line: {						
						lineWidth: 3,
						marker: {
							enabled: false,
							states: {
								hover: {
									enabled: true,
									radius: 5
								}
							}
						},
						shadow: false,
						states: {
							hover: {
								lineWidth: 3
							}
						}
					}
				},		
				series: [
				 {
					type: 'spline',
					name: '漏失率(%)',
					color:  '#ED561B',
					data: ChartData.flowRate.arrData
				} 
				,
				 {
					type: 'spline',
					yAxis: 1,
					name: '输入水量(m³/h)',
					color:  '#058DC7',
					data: ChartData.inflowRate.arrData
				} 
				,
				 {
					type: 'spline',
					yAxis: 1,
					name: '输出水量(m³/h)',
					color:  '#50B432',
					data: ChartData.outflowRate.arrData
				} 
				]
			});
		}
		
		function reDrowCruve(){
			alert(chart);
			 chart.redraw();
		}
			