
function getFormatDate(date) {
    var sep1 = "-";
    var sep2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    return date.getFullYear() + sep1 + month + sep1 + strDate
        + " " + date.getHours() + sep2 + date.getMinutes()
        + sep2 + date.getSeconds();
}
function update(array) {
    var nodes = document.getElementById("real-time-monitor").getElementsByClassName("layui-progress-bar");
    for(var i=0;i!==nodes.length;++i)
    {
        var value = array[i];
        if(value < 20){
            nodes[i].setAttribute("class","layui-progress-bar layui-bg-blue");
        }else if(value < 50) {
            nodes[i].setAttribute("class","layui-progress-bar layui-bg-green");
        }else if(value < 80) {
            nodes[i].setAttribute("class","layui-progress-bar layui-bg-orange");
        }else {
            nodes[i].setAttribute("class","layui-progress-bar layui-bg-red");
        }
        nodes[i].setAttribute("lay-percent",value+"%");
    }
}
function openWebSocket(element,webSocketId) {

    var url = "ws://localhost:10086";
    var socket = new WebSocket(url + '/websocket/'+ webSocketId);
    socket.onerror = function (event)
    {
        console.log("连接失败");
    };
    socket.onopen = function (event)
    {
        console.log("已连接");
    };
    socket.onmessage = function (event) {
        //获取两个随机数
        update(JSON.parse(event.data));
        element.render('progress');
    }
}

layui.define(function (e) {
    //实时监控
    layui.use('element', function(){
        var element = layui.element;
        var mac = layui.$("#mac-address").val();
        openWebSocket(element,mac);
    });
    //轮播
    layui.use(["admin", "carousel"], function () {
        var e = layui.$, t = (layui.admin, layui.carousel), a = layui.element, i = layui.device();
        e(".layadmin-carousel").each(function () {
            var a = e(this);
            t.render({
                elem: this,
                width: "100%",
                arrow: "none",
                interval: a.data("interval"),
                autoplay: a.data("autoplay") === true,
                trigger: i.ios || i.android ? "click" : "hover",
                anim: a.data("anim")
            })
        });
        a.render("progress")
    });
    //echart曲线图
    layui.use(["carousel", "echarts"], function () {
        var children = layui.$("#LAY-index-dataview").children("div"), charts = [],
            dataMap = {
                "time":[],
                "cpu":{
                    "user":[],
                    "sys":[],
                    "ioWait":[],
                    "idle":[],
                    "other":[]
                },
                "ram":{
                    "actual":[],
                    "cached":[],
                    "free":[]
                },
                "disk":{
                    "in":[],
                    "out":[]
                },
                "net":{
                    "in":[],
                    "out":[]
                },
                "tcp":{
                    "ERROR_STATUS":[],
                    "TIME_WAIT":[],
                    "FIN_WAIT2":[],
                    "ESTABLISHED":[],
                    "CLOSE_WAIT":[],
                    "CLOSE":[],
                    "CLOSING":[],
                    "FIN_WAIT1":[],
                    "LAST_ACK":[],
                    "LISTEN":[],
                    "SYN_RECV":[],
                    "SYN_SEND":[]
                }
            },
            options = [{
                title: {text: "CPU监控详情", textStyle: {fontSize: 14}},
                tooltip: {trigger: "axis"},
                legend: {data:['User','System','IOWait','Other','Idle']},
                xAxis: [{
                    type: "category",
                    boundaryGap: false,
                    data: dataMap["time"]
                }],
                yAxis: [{
                    type: "value",
                    name: 'CPU使用率(%)',
                    min: 0,
                    max: 100
                }],
                dataZoom: [{
                    type: 'inside',
                    start: 0,
                    end: 20
                }, {
                    start: 0,
                    end: 20,
                    handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: '80%',
                    handleStyle: {
                        color: '#fff',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    }
                }],
                series: [{
                    name: "User",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["cpu"]["user"]
                }, {
                    name: "System",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["cpu"]["sys"]
                },{
                    name: "IOWait",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["cpu"]["ioWait"]
                },{
                    name: "Other",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["cpu"]["other"]
                },{
                    name: "Idle",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["cpu"]["idle"]
                }]
            },{
                title: {text: "内存监控详情", textStyle: {fontSize: 14}},
                tooltip: {trigger: "axis"},
                legend: {data: ["Actual Used", "Cached+Buffers", "Free"]},
                xAxis: [{
                    type: "category",
                    boundaryGap: false,
                    data: dataMap["time"]
                }],
                yAxis: [{
                    type: "value",
                    name: '内存利用率(%)',
                    min:0,
                    max:100
                }],
                series: [{
                    name: "Actual Used",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["ram"]["actual"]
                }, {
                    name: "Cached+Buffers",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["ram"]["cached"]
                }, {
                    name: "Free",
                    type: "line",
                    stack: '总量',
                    smooth: true,
                    itemStyle: {normal: {areaStyle: {type: "default"}}},
                    data: dataMap["ram"]["free"]
                }]
            },{
                title: {text: "流量监控详情", textStyle: {fontSize: 14}},
                tooltip: {trigger: "axis"},
                legend: {data: ["进站", "出站"]},
                xAxis: [{
                    type: "category",
                    boundaryGap: false,
                    data: dataMap["time"]
                }],
                yAxis: [{
                    type: "value",
                    name: '速度(kb/s)'
                }],
                series: [{
                    name: "进站",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["net"]["in"]
                }, {
                    name: "出站",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["net"]["out"]
                }]
            },{
                title: {text: "磁盘IO详情", textStyle: {fontSize: 14}},
                tooltip: {trigger: "axis"},
                legend: {data: ["读", "写"]},
                xAxis: [{
                    type: "category",
                    boundaryGap: false,
                    data: dataMap["time"]
                }],
                yAxis: [{
                    type: "value",
                    name: '速率(kb/s)'
                }],
                series: [{
                    name: "读",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["disk"]["in"]
                }, {
                    name: "写",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["disk"]["out"]
                }]
            },{
                title: {text: "TCP详情", textStyle: {fontSize: 14}},
                tooltip: {trigger: "axis"},
                legend: {
                    data: ["ERROR","ESTABLISHED", "TIME_WAIT", "FIN_WAIT2","CLOSE_WAIT","CLOSE","CLOSING",
                        "FIN_WAIT1","LAST_ACK","LISTEN","SYN_RECV","SYN_SEND"],textStyle: {fontSize: 10}
                },
                xAxis: [{
                    type: "category",
                    boundaryGap: !1,
                    data: dataMap["time"]
                }],
                yAxis: [{
                    type: "value",
                    name: '数量'
                }],
                series: [{
                    name: "ERROR",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["ERROR_STATUS"]
                }, {
                    name: "ESTABLISHED",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["ESTABLISHED"]
                }, {
                    name: "TIME_WAIT",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["TIME_WAIT"]
                }, {
                    name: "FIN_WAIT2",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["FIN_WAIT2"]
                }, {
                    name: "CLOSE_WAIT",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["CLOSE_WAIT"]
                }, {
                    name: "CLOSE",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["CLOSE"]
                }, {
                    name: "CLOSING",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["CLOSING"]
                }, {
                    name: "FIN_WAIT1",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["FIN_WAIT1"]
                }, {
                    name: "LAST_ACK",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["LAST_ACK"]
                }, {
                    name: "LISTEN",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["LISTEN"]
                }, {
                    name: "SYN_RECV",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["SYN_RECV"]
                }, {
                    name: "SYN_SEND",
                    type: "line",
                    smooth: true,
                    itemStyle: {normal: {}},
                    data: dataMap["tcp"]["SYN_SEND"]
                }]
            }],r = function (e) {
                charts[e] = layui.echarts.init(children[e], layui.echartsTheme);
                charts[e].setOption(options[e]);
                window.onresize = charts[e].resize;
            };
        if (children[0]) {
            r(0);
            var index = 0;
            layui.carousel.on("change(LAY-index-dataview)", function (e) {
                r(index = e.index)
            });
            layui.admin.on("side", function () {
                setTimeout(function () {r(index)}, 300)
            });
            layui.admin.on("hash(tab)", function () {
                layui.router().path.join("") || r(index)
            });
            var stop = new Date();
            var start = new Date();
            start.setDate(start.getDate()-3600*24);
            layui.$.ajax({
                url:"/monitor/findAllByTime",
                type:"POST",
                async:false,
                dataType:"json",
                data:{
                    "mac":"6C71D92E3613",
                    "start":getFormatDate(start),
                    "stop":getFormatDate(stop)
                },
                success:function(result) {
                    for(var i=0;i!==result.length;++i)
                    {
                        var o = result[i];
                        dataMap.time.push(o["t"].replace(" ","\n"));
                        dataMap.cpu.user.push(parseFloat(o["001"]));
                        dataMap.cpu.sys.push(parseFloat(o["002"]));
                        dataMap.cpu.ioWait.push(parseFloat(o["003"]));
                        dataMap.cpu.other.push(parseFloat(o["004"]));
                        dataMap.cpu.idle.push(parseFloat(o["005"]));
                        dataMap.ram.actual.push(parseFloat(o["011"]));
                        dataMap.ram.cached.push(parseFloat(o["012"]));
                        dataMap.ram.free.push(parseFloat(o["013"]));
                        dataMap.disk.in.push(parseInt(o["021"])/1024);
                        dataMap.disk.out.push(parseInt(o["022"])/1024);
                        dataMap.net.out.push(parseInt(o["031"])/1024);
                        dataMap.net.in.push(parseInt(o["032"])/1024);
                        dataMap.tcp.ERROR_STATUS.push(parseInt(o["041"]));
                        dataMap.tcp.ESTABLISHED.push(parseInt(o["042"]));
                        dataMap.tcp.SYN_SEND.push(parseInt(o["043"]));
                        dataMap.tcp.SYN_RECV.push(parseInt(o["044"]));
                        dataMap.tcp.FIN_WAIT1.push(parseInt(o["045"]));
                        dataMap.tcp.FIN_WAIT2.push(parseInt(o["046"]));
                        dataMap.tcp.TIME_WAIT.push(parseInt(o["047"]));
                        dataMap.tcp.CLOSE.push(parseInt(o["048"]));
                        dataMap.tcp.CLOSE_WAIT.push(parseInt(o["049"]));
                        dataMap.tcp.LAST_ACK.push(parseInt(o["050"]));
                        dataMap.tcp.LISTEN.push(parseInt(o["051"]));
                        dataMap.tcp.CLOSING.push(parseInt(o["052"]));
                    }
                    r(index);
                },
                error:function(msg){
                    console.log("error:"+msg);
                }
            });
        }
    });
    e("console", {})
});