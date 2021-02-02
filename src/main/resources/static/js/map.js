//初始化城市数据
var cityList;
var cityArea = $(".layui-textarea>.layui-breadcrumb");
var nowCity;
var nowCityName;
var from = "";//定义起点
var to = "";//定义终点
var myAddress;
var path = [];//线路规划数组
var route;//规划线路

//乘车策略map
var policy;
//公交站点列表
var markers = [];
$(function () {
    $.ajax({
        url: "/city/getCity",
        type: "post",
        data: null,
        dataType: "json",
        success: function (date) {
            cityList = date;
        },
    })

});

//显示比例尺
var scale = new AMap.Scale({
        visible: true
    }),

    map = new AMap.Map('container', {
        resizeEnable: true,//是否监控地图容器尺寸变化
        mapStyle: 'amap://styles/macaron', //设置地图的显示样式
        zoom: 13,//地图显示的缩放级别
        // center: [118.178322, 24.492585],//初始化地图中心点
        // viewMode: '3D',
        // pinch: 45
    });
map.addControl(scale);
//输入提示
auto = new AMap.AutoComplete({
    input: "tipinput"
});
//添加3d罗盘
// AMap.plugin([
//     'AMap.ControlBar',
// ], function(){
//     // 添加 3D 罗盘控制
//     map.addControl(new AMap.ControlBar());
// });
//步行导航1
var walking1 = new AMap.Walking({
    map: map,
    hideMarkers: true,
    autoFitView: true,
});
//步行导航2
var walking2 = new AMap.Walking({
    map: map,
    hideMarkers: true,
    autoFitView: true,
});
//根据ip精确定位
var options = {
    // 'showButton': true,//是否显示定位按钮
    // 'buttonPosition': 'RB',//定位按钮的位置
    /* LT LB RT RB */
    // 'buttonOffset': new AMap.Pixel(10, 20),//定位按钮距离对应角落的距离
    'showMarker': true,//是否显示定位点
    'markerOptions': {//自定义定位点样式，同Marker的Options
        'offset': new AMap.Pixel(-18, -36),
        'content': '<img src="https://a.amap.com/jsapi_demos/static/resource/img/user.png" style="width:36px;height:36px"/>'
    },
    'showCircle': true,//是否显示定位精度圈
    'circleOptions': {//定位精度圈的样式
        'strokeColor': '#0093FF',
        'noSelect': true,
        'strokeOpacity': 0.5,
        'strokeWeight': 1,
        'fillColor': '#02B0FF',
        'fillOpacity': 0.25
    }
};

AMap.plugin(["AMap.Geolocation"], function () {
    var geolocation = new AMap.Geolocation(options);
    map.addControl(geolocation);
    geolocation.getCurrentPosition(function (status, result) {
        //精确定位返回结果
        getPlace(result.position, 1);
        var timeoutBox = setInterval(function () {
            if (myAddress != undefined) {
                clearInterval(timeoutBox);
            }
        }, 100);

    });
    geolocation.getCityInfo(function (status, result) {
        if (status === 'complete') {
            $(".city-title>b").text(result.city);
            $(".layui-field-title>legend").text("当前城市：" + result.city);
            nowCity = result.citycode;
            nowCityName = result.city;
            //加载天气查询插件
            AMap.plugin('AMap.Weather', function () {
                //创建天气查询实例
                var weather = new AMap.Weather();
                //执行实时天气信息查询
                weather.getForecast(result.adcode, function (err, data) {
                    $(".weather-info").html(data.forecasts[0].dayWeather + '<br> ' + data.forecasts[0].nightTemp + '/' + data.forecasts[0].dayTemp + '℃')
                });
            });

        } else {
            $(".city-title>b").text("定位失败!");
            $(".weather-info").html("")
        }
    });

});

var geocoder = new AMap.Geocoder({
    city: nowCity, //城市设为北京，默认：“全国”
    radius: 1000 //范围，默认：500
});

//初始化加载所有站点
AllStation();
document.getElementById('search').onclick = stationSearch;

//根据搜索信息定位到站点、地理位置
function stationSearch() {
    var stationKeyWord = document.getElementById('tipinput').value;
    if (!stationKeyWord) return;
    var flag = false;
    for (i = 0, len = markers.length; i < len; i++) {
        if (markers[i]._originOpts.title.includes(stationKeyWord)) {
            flag = true;
            var dot = markers[i];
            map.setCenter([dot._position.lng, dot._position.lat], true, 100)
        }
    }
    if (flag == false) {
        AMap.plugin(["AMap.PlaceSearch"], function () {
            //构造地点查询类
            var placeSearch = new AMap.PlaceSearch({
                pageSize: 1, // 单页显示结果条数
                pageIndex: 1, // 页码
                city: nowCity, // 兴趣点城市
                citylimit: true,  //是否强制限制在设置的城市内搜索
                map: map, // 展现结果的地图实例
                autoFitView: true // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
            });
            //关键字查询
            placeSearch.search(stationKeyWord);
        });
    }
}

/*公交站点查询返回数据解析*/
function AllStation() {
//从本地获取公交站点数据
    $.ajax({
        url: "/site/getSite",
        type: "post",
        data: null,
        dataType: "json",
        success: function (data) {
            var searchNum = data.length;
            if (searchNum > 0) {
                for (var i = 0; i < searchNum; i++) {
                    var marker = new AMap.Marker({
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
    })
}


//城市点击
layui.use('layer', function () { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer;
    $("#citybox").click(function () {
        layer.open({
            type: 1,
            title: "城市列表",
            area: ['400px', '500px'],
            offset: '200px',
            content: $("#all_city"),
            cancel: function () {
                // 你点击右上角 X 取消后要做什么
            },
            success: function () {
                cityArea.empty();
                for (var key in cityList) {
                    cityArea.append('<li onclick="chooseCity(this)">' + cityList[key].cityName + '</li>')
                }
            }
        });
    });
});

//点击字母事件
function initials(cityCell) {
    cityArea.empty();
    for (var key in cityList) {
        if (cityList[key].initials == cityCell.innerText) {
            cityArea.append('<li onclick="chooseCity(this)">' + cityList[key].cityName + '</li>')
        }
    }
}

//点击城市事件
function chooseCity(cityName) {

    $(".layui-field-title>legend").text("当前城市：" + cityName.innerText);
    AMap.plugin(['AMap.PlaceSearch'], function () {
        var placeSearch = new AMap.PlaceSearch({
            map: map
        });  //构造地点查询类
        for (var key in cityList) {
            if (cityList[key].cityName == cityName.innerText) {
                placeSearch.setCity(cityList[key].adcode);
                placeSearch.search(cityName.innerText)
            }
        }
    });
    //改变天气变化
    $(".city-title>b").text(cityName.innerText);
    //加载天气查询插件
    AMap.plugin('AMap.Weather', function () {
        //创建天气查询实例
        var weather = new AMap.Weather();
        //执行实时天气信息查询
        weather.getForecast(cityName.innerText, function (err, data) {
            $(".weather-info").html(data.forecasts[0].dayWeather + '<br> ' + data.forecasts[0].nightTemp + '/' + data.forecasts[0].dayTemp + '℃')
        });
    });

    layer.closeAll();
}


//打开线路栏
function openSeach() {
    if ($(".dirbox").css('display') === 'none') {
        $(".dirbox").slideToggle("normal");
    }
    //加载历史记录

    //设置起点
    if (from != null) {
        $("#dir_from_ipt").val(from);
    }
    if (to != null) {
        $("#dir_to_ipt").val(to);
    }
}


//根据经纬度找位置方法
function getPlace(lnglat, index) {
    geocoder.getAddress(lnglat, function (status, result) {
        if (status === 'complete' && result.regeocode) {
            if (index == 1) {
                if (myAddress == undefined) {
                    myAddress = result.regeocode.formattedAddress;
                }
            } else if (index == 2) {
                from = result.regeocode.formattedAddress;
            } else if (index == 3) {
                to = result.regeocode.formattedAddress;
            }
        } else {
            log.error('根据经纬度查询地址失败')
        }
    })
}

//根据位置找经纬度方法
function getLnglat(place) {
    var lnglat;

    geocoder.getLocation(place, function (status, result) {
        if (status === 'complete' && result.geocodes.length) {
            lnglat = result.geocodes[0].location
        } else {
            log.error('根据地址查询位置失败');
        }
    });
}

var markerStart;
var markerEnd;
var polyline;//线路
//自定义菜单类
function ContextMenu(map) {
    var me = this;

    this.contextMenuPositon = null;

    var content = [];

    content.push("<div class='info context_menu'>");
    content.push("<p onclick='menu.addStart()'><img src='/imgs/start_marker.png' style='height: 35px;margin-right: 10px'>设为起点</p>");
    content.push("<p onclick='menu.addEnd()'><img src='/imgs/end_marker.png' style='height: 35px;margin-right: 10px'>设为终点</p>");
    content.push("<p onclick='menu.clearRoute()'><img src='/imgs/clearRoute.png' style='height: 35px;margin-right: 10px'>清除路线</p>");
    content.push("</div>");

    //通过content自定义右键菜单内容
    this.contextMenu = new AMap.ContextMenu({isCustom: true, content: content.join('')});

    //地图绑定鼠标右击事件——弹出右键菜单
    map.on('rightclick', function (e) {
        me.contextMenu.open(map, e.lnglat);
        me.contextMenuPositon = e.lnglat; //右键菜单位置
    });
}

ContextMenu.prototype.addStart = function () {  //右键菜单添加起点标记
    if (markerStart != undefined) {
        map.remove(markerStart);
    }
    markerStart = new AMap.Marker({
        icon: new AMap.Icon({
            image: '/imgs/start_marker.png',
            imageSize: new AMap.Size(32, 40)
        }),
        offset: new AMap.Pixel(-16, -40),
        map: map,
        position: this.contextMenuPositon //基点位置
    });
    //绘制路线起点
    getPlace([markerStart._position.lng, markerStart._position.lat], 2);
    setTimeout(function () {
        openSeach();
    }, 500);
    drawLine();
    this.contextMenu.close();
};
ContextMenu.prototype.addEnd = function () {  //右键菜单添加终点标记
    if (markerEnd != undefined) {
        map.remove(markerEnd);
    }
    markerEnd = new AMap.Marker({
        icon: new AMap.Icon({
            image: '/imgs/end_marker.png',
            imageSize: new AMap.Size(32, 40)
        }),
        offset: new AMap.Pixel(-16, -40),
        map: map,
        position: this.contextMenuPositon //基点位置
    });
    //绘制路线终点
    getPlace([markerEnd._position.lng, markerEnd._position.lat], 3);
    setTimeout(function () {
        openSeach();
    }, 500);
    drawLine();
    this.contextMenu.close();
};
ContextMenu.prototype.clearRoute = function () {  //清除记录
    //清除标记
    if (markerStart != undefined) {
        map.remove(markerStart);
    }
    if (markerEnd != undefined) {
        map.remove(markerEnd);
    }
    //清除路线
    map.remove(polyline);
    walking1.clear()
    walking2.clear();
    //清除搜索框
    $("#dir_from_ipt").val("");
    $("#dir_to_ipt").val("");
    from = null;
    to = null;
    //清除乘车方案

    //添加历史记录

    this.contextMenu.close();
};

//创建右键菜单
var menu = new ContextMenu(map);


//为地图注册click事件获取鼠标点击出的经纬度坐标
map.on('click', function (e) {
    //左键获取经纬度
    document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat()
});

//反转线路
function reversal() {
    $("#dir_from_ipt").val(to);
    $("#dir_to_ipt").val(from);
    from = $("#dir_from_ipt").val();
    to = $("#dir_to_ipt").val();
    if (markerStart != undefined) {
        var markerStart1 = markerEnd;
        var markerStart2 = markerStart;
        map.remove(markerStart);
    }
    markerStart = new AMap.Marker({
        icon: new AMap.Icon({
            image: '/imgs/start_marker.png',
            size: new AMap.Size(32, 40),
            imageSize: new AMap.Size(32, 40)
        }),
        map: map,
        position: markerStart1._position //基点位置
    });
    //绘制路线起点
    drawLine();
    setTimeout(function () {
        if (markerEnd != undefined) {
            var markerEnd1 = markerStart2;
            map.remove(markerEnd);
        }
        markerEnd = new AMap.Marker({
            icon: new AMap.Icon({
                image: '/imgs/end_marker.png',
                size: new AMap.Size(32, 40),
                imageSize: new AMap.Size(32, 40)
            }),
            map: map,
            position: markerEnd1._position //基点位置
        });
        //绘制路线终点
        drawLine();
    }, 500)
}


//绘制初始路径
function drawLine() {
    if (markerStart != undefined && markerEnd != undefined) {
        if (polyline){
            map.remove(polyline);
        }
        walking1.clear();
        walking2.clear();
        busPolicy(markerStart, markerEnd)
    }
}

//关闭线路栏
function closeBox() {
    if ($(".dirbox").css('display') === 'block') {
        $(".dirbox").slideToggle("normal");
    }
    menu.clearRoute();
}

function closeBox1() {
    menu.clearRoute();
}

//点叉清除输入内容
function clearInput(node) {
    node.parentNode.childNodes[2].value = "";
}


//根据起终点坐标规划步行路线
function walkLine(marker1, marker2,walking) {
    walking.search(marker1.getPosition(), marker2.getPosition(), function (status, result) {
        // result即是对应的步行路线数据信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_WalkingResult
        if (status === 'complete') {
            console.log(result);
            log.success('绘制步行路线完成')
        } else {
            log.error('步行路线数据查询失败' + result)
        }
    });
}

//根据两点计算之间的距离，用于判断位置与公交站之间哪个近
function computeDis(marker1, marker2) {
    return Math.round(marker1.getPosition().distance(marker2.getPosition()));
}


//后台处理返回方案数据
function busPolicy(startMarker, endMarker) {
    //起点终点坐标数组
    console.log(startMarker);
    console.log(endMarker);
    let startPos = [startMarker._position.lng, startMarker._position.lat];//起点用
    let endPos = [endMarker._position.lng, endMarker._position.lat];//终点用
    console.log(startPos);
    console.log(endPos);
    var near = 0;//最短路线
    var index;//下标
    var type;//哪个数据
    let marker1 = new AMap.Marker();
    let marker2 = new AMap.Marker();
    //同步请求数据
    $.ajax({
        async: false,
        url: "/routeController/getRoutes",
        type: "post",
        data: {"startPos": startPos, "endPos": endPos},
        dataType: "json",
        traditional: true,
        success: function (data) {
            console.log(data);
            if (data[0] != null) {
                for (let i = 0; i < data[0].length; i++) {
                    marker1.setPosition(data[0][i].list[0]);
                    marker2.setPosition(data[0][i].list[list.length - 1]);
                    let all = computeDis(startMarker, marker1) + computeDis(endMarker, marker2);
                    if (near === 0 || near > all) {
                        near = all;
                        index = i;
                        type = false;
                    }
                }
            }
            if (data[1] != null) {
                for (let i = 0; i < data[1].length; i++) {
                    marker1.setPosition(data[1][i].list[0]);
                    marker2.setPosition(data[1][i].list[data[1][i].list.length - 1]);
                    let all = computeDis(startMarker, marker1) + computeDis(endMarker, marker2);
                    if (near === 0 || near > all) {
                        near = all;
                        index = i;
                        type = true;
                    }
                }
            }
            if (data[0] != null || data[1] != null) {
                //显示路线
                if (type = true) {
                    marker1.setPosition(data[1][index].list[0]);
                    marker2.setPosition(data[1][index].list[data[1][index].list.length - 1]);
                    //绘制第一条线路
                     polyline = new AMap.Polyline({
                        routeId: 1,
                        path: data[1][index].list,
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
                    walkLine(startMarker, marker1,walking1);
                    walkLine(marker2, endMarker,walking2);
                } else {
                    marker1.setPosition(data[0][index].list[0]);
                    marker2.setPosition(data[0][index].list[data[1][index].list.length - 1]);
                    //绘制第一条线路
                     polyline = new AMap.Polyline({
                        routeId: 1,
                        path: data[0][index].list,
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
                    walkLine(startMarker, marker1,walking1);
                    walkLine(marker2, endMarker,walking2);
                }



            }
        }
    });
}

//方案显示
function busPolicyView(){

    $(".plan_wrap").append();
    if ($(".plan_wrap").css('display') === 'none') {
        $(".plan_wrap").slideToggle("normal");
    }

}