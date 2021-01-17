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
var drivingOption = {
    policy: AMap.DrivingPolicy.LEAST_DISTANCE, // 其它policy参数请参考 https://lbs.amap.com/api/javascript-api/reference/route-search#m_DrivingPolicy
    ferry: 1, // 是否可以使用轮渡
    map: map,
    hideMarkers: true,
    autoFitView: true,
};
// 构造路线导航类
var driving = new AMap.Driving(drivingOption)

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
                        offset: new AMap.Pixel(-12, -12),
                        position: [data[i].longitude,data[i].latitude],
                        map: map,
                        title: data[i].siteName,
                        siteId:data[i].siteId
                    });

                    marker.info = new AMap.InfoWindow({
                        content: data[i].siteName,
                        offset: new AMap.Pixel(0, -12)
                    });
                    marker.on('click', function (e) {
                        e.target.info.open(map, e.target.getPosition())
                    });
                    console.log(marker)
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
            size: new AMap.Size(32, 40),
            imageSize: new AMap.Size(32, 40)
        }),
        map: map,
        position: this.contextMenuPositon //基点位置
    });
    //绘制路线起点
    drawLine(markerStart);
    getPlace([markerStart._position.lng, markerStart._position.lat], 2);
    setTimeout(function () {
        openSeach();
    }, 500);
    this.contextMenu.close();
};
ContextMenu.prototype.addEnd = function () {  //右键菜单添加终点标记
    if (markerEnd != undefined) {
        map.remove(markerEnd);
    }
    markerEnd = new AMap.Marker({
        icon: new AMap.Icon({
            image: '/imgs/end_marker.png',
            size: new AMap.Size(32, 40),
            imageSize: new AMap.Size(32, 40)
        }),
        map: map,
        position: this.contextMenuPositon //基点位置
    });
    //绘制路线终点
    drawLine(markerEnd);
    getPlace([markerEnd._position.lng, markerEnd._position.lat], 3);
    setTimeout(function () {
        openSeach();
    }, 500);
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
    driving.clear();
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
    from=$("#dir_from_ipt").val();
    to= $("#dir_to_ipt").val();
    if (markerStart != undefined) {
        var markerStart1 = markerEnd;
        var markerStart2=markerStart;
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
    drawLine(markerStart);
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
        drawLine(markerEnd);
    },500)
}


//绘制初始路径
var arr = new Array();

function drawLine(Marker) {
    switch (Marker._opts.icon._opts.image) {
        case "/imgs/start_marker.png":
            switch (path.length) {
                case 0:
                    path.splice(0, 0, [Marker._position.lng, Marker._position.lat]);
                    break;
                default:
                    path.splice(0, 1, [Marker._position.lng, Marker._position.lat]);
                    break;
            }
            break;
        case "/imgs/end_marker.png":
            switch (path.length) {
                case 0:
                    path.splice(1, 0, [Marker._position.lng, Marker._position.lat]);
                    break;
                default:
                    path.splice(1, 1, [Marker._position.lng, Marker._position.lat]);
                    break;
            }
            path.splice(1, 1, [Marker._position.lng, Marker._position.lat]);
            break;
    }
    if (path.length == 2) {
        driving.clear();
        // 根据起终点经纬度规划驾车导航路线
        driving.search(path[0], path[1], function (status, result) {
            // result 即是对应的驾车导航信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_DrivingResult
            if (status === 'complete') {
                log.success('绘制驾车路线完成')
                console.log(result.routes)
            } else {
                log.error('获取驾车数据失败：' + result)
            }
        });
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


function f() {
    $.ajax({
        url: "/site/getSite",
        type: "post",
        data: null,
        dataType: "json",
        success: function (data) {

        },
    })
}