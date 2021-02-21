var routes = new Array();//线路点集合
var allroutes=new Array();//线路集合
var markers = [];
var buses = [];
//显示比例尺
var scale = new AMap.Scale({
        visible: true
    }),
    map = new AMap.Map('container', {
        resizeEnable: true,//是否监控地图容器尺寸变化
        mapStyle: 'amap://styles/macaron', //设置地图的显示样式
        zoom: 13,//地图显示的缩放级别
        // center: [118.178322, 24.492585],//初始化地图中心点
        viewMode: '3D',
        pinch: 45
    });
map.addControl(scale);

AMap.plugin(['AMap.ControlBar'], function () {
    // 添加 3D 罗盘控制
    map.addControl(new AMap.ControlBar());
});
StationsAndRouteAndBus();

function StationsAndRouteAndBus() {
//从本地获取公交站点数据
    $.ajax({
        url: "/site/getSite",
        type: "post",
        data: null,
        dataType: "json",
        success: function (data) {

            let searchNum = data.length;
            if (searchNum > 0) {
                for (let i = 0; i < searchNum; i++) {
                    let marker = new AMap.Marker({
                        icon: new AMap.Icon({
                            image: '//a.amap.com/jsapi_demos/static/resource/img/pin.png',
                            imageSize: new AMap.Size(24, 24)
                        }),
                        offset: new AMap.Pixel(-12, -24),
                        position: [data[i].longitude, data[i].latitude],
                        map: map,
                        title: data[i].siteName,
                        siteId: data[i].siteId
                    });
                    marker.info = new AMap.InfoWindow({
                        content: data[i].siteName,
                        offset: new AMap.Pixel(0, -24)
                    });
                    marker.on('click', function (e) {
                        e.target.info.open(map, e.target.getPosition())
                    });
                    markers.push(marker);
                }
                map.setFitView();
            }
        },
    });
    //画出所有线路
    $.ajax({
        url: "/routeController/Routes",
        type: "post",
        data: null,
        dataType: "json",
        success: function (data) {
            console.log(data);
            if (data != null) {
                allroutes=data;
                for (let i = 0; i < data.length; i++) {
                    routes.push(data[i].list);
                    // $.ajax({
                    //     url: "/WXAppController/refreshBusInf",
                    //     type: "post",
                    //     data: {"routeId": data[i].routeId, "siteId": data[i].startSite},
                    //     dataType: "json",
                    //     success: function (data) {
                    //         console.log(data)
                    //     }
                    // });
                    // $.ajax({
                    //     url: "/WXAppController/getBusInfoTime",
                    //     type: "post",
                    //     data: "routeId="+data[i].routeId,
                    //     // , "siteId": routes[i].startSite
                    //     dataType: "json",
                    //     success: function (data) {
                    //         console.log(data)
                    //     }
                    // });
                    let polyline = new AMap.Polyline({
                        routeId: data[i].routeId,
                        path: data[i].list,
                        // isOutline: true,//显示描边
                        // outlineColor: '#ffeeff',
                        // borderWeight: 3,
                        showDir: true,//是否延路径显示白色方向箭头,默认false。建议折线宽度大于6时使用
                        strokeColor: "#05aa2e",//线条颜色
                        strokeOpacity: 1,//轮廓线透明度，取值范围 [0,1] ，0表示完全透明，1表示不透明。默认为0.5
                        strokeWeight: 6,//轮廓线宽度
                        // 折线样式还支持 'dashed'
                        strokeStyle: "solid",
                        // strokeStyle是dashed时有效
                        // strokeDasharray: [10, 5],  //勾勒形状轮廓的虚线和间隙的样式，此属性在strokeStyle 为dashed 时有效， 此属性在ie9+浏览器有效 取值： 实线： [0,0,0] 虚线： [10,10] ， [10,10] 表示10个像素的实线和10个像素的空白（如此反复）组成的虚线 点画线： [10,2,10] ， [10,2,10] 表示10个像素的实线和2个像素的空白 + 10个像素的实线和10个像素的空白 （如此反复）组成的虚线
                        lineJoin: 'round',//折线拐点的绘制样式，默认值为'miter'尖角，其他可选值：'round'圆角、'bevel'斜角
                        lineCap: 'round',//折线两端线帽的绘制样式，默认值为'butt'无头，其他可选值：'round'圆头、'square'方头
                        zIndex: 50,
                    });
                    map.add(polyline);
                    map.setFitView();
                }
            }

        },
    });
    setInterval("busRun(allroutes)",1000);

}

function busRun(routes) {
    map.remove(buses);
    buses=[];
    for (let i = 0; i < routes.length; i++) {
            $.ajax({
                // async: false,
                url: "/WXAppController/getBusInfoTime",
                type: "post",
                data: "routeId=" + routes[i].routeId,
                dataType: "json",
                success: function (data) {
                    console.log(data);
                    if (data.busInfs.length != 0) {
                        for (let j = 0; j < data.busInfs.length; j++) {
                            let marker = new AMap.Marker({
                                icon: new AMap.Icon({
                                    image: "/imgs/bus.png",
                                    imageSize: new AMap.Size(24, 24)
                                }),
                                offset: new AMap.Pixel(-12, -24),
                                position: [data.busInfs[j].site.longitude, data.busInfs[j].site.latitude],
                                map: map,
                                title: data.busInfs[j].busName,
                            });
                            marker.info = new AMap.InfoWindow({
                                content: data.busInfs[j].busName+"<br>"+data.busInfs[j].carNum+"<br>"+data.busInfs[j].busId+"<br>"+data.busInfs[j].busState,
                                offset: new AMap.Pixel(0, -24)
                            });
                            marker.on('mouseover', function (e) {
                                e.target.info.open(map, e.target.getPosition())
                            });
                            buses.push(marker);
                        }
                    }
                }
            });
        }

}

