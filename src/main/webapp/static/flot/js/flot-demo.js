

$(function() {
    var barOptions = {
        series: {
            lines: {
                show: true,
                lineWidth: 2,
                fill: true,
                fillColor: {
                    colors: [{
                        opacity: 0.0
                    }, {
                        opacity: 0.0
                    }]
                }
            }
        },
        yaxis: {
            axisLabel: "用户留存率(%)"
        },
        xaxes: [{
            mode: 'time'
        }],
        colors: ["#1ab394"],
        grid: {
            color: "#999999",
            hoverable: true,
            clickable: true,
            tickColor: "#D4D4D4",
            borderWidth:0
        },
        legend: {
            show: true,            
            position: 'sw'
        },
        tooltip: true,
        tooltipOpts: {
            content: "日期: %x, %s为: %y %"
        }
    };
    var barData = {
        label: "7日留存率",
        points: {  show: true,radius: 3 },
        lines: { show: true },
        data: [
               [1167692400000, 31.05],
               [1170025200000, 64.01],
               [1170111600000, 46.97],
               [1170198000000, 28.14],
               [1170284400000, 88.14],
               [1170370800000, 69.02],
               [1170630000000, 158.74],
               [1170716400000, 28.88],
               [1170802800000, 97.71],
               [1170889200000, 49.71],
               [1170975600000, 79.89],
               [1171234800000, 137.81],
               [1171321200000, 9.06],
               [1171407600000, 38.00],
               [1171494000000, 7.99]
        ]
    };
    var barData1 = {
            label: "30日留存率",
            points: {  show: true ,radius: 3 },
            lines: { show: true },
            data: [
                   [1167692400000, 41.05],
                   [1170025200000, 54.01],
                   [1170111600000, 56.97],
                   [1170198000000, 58.14],
                   [1170284400000, 58.14],
                   [1170370800000, 59.02],
                   [1170630000000, 58.74],
                   [1170716400000, 58.88],
                   [1170802800000, 57.71],
                   [1170889200000, 59.71],
                   [1170975600000, 59.89],
                   [1171234800000, 57.81],
                   [1171321200000, 59.06],
                   [1171407600000, 58.00],
                   [1171494000000, 57.99]
            ]
        };
    $.plot($("#flot-line-chart"), [barData,barData1], barOptions);

});
