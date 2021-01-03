//初始化城市数据
var cityList;
var cityArea = $(".layui-textarea>.layui-breadcrumb");
$(function () {
    $.ajax({
        url: "/getMap/getCity",
        type: "post",
        data: null,
        dataType: "json",
        success: function (date) {
            console.log(date);
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
        zoom: 13 //地图显示的缩放级别
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
//鼠标工具类
var mouseTool = new AMap.MouseTool(map);
//监听draw事件可获取画好的覆盖物
var overlays = [];
mouseTool.on('draw', function (e) {
    overlays.push(e.obj);
})

//图形绘制工具
function draw(type) {
    switch (type) {
        case 'marker': {
            mouseTool.marker({
                //同Marker的Option设置
            });
            break;
        }
        case 'polyline': {
            mouseTool.polyline({
                strokeColor: '#80d8ff'
                //同Polyline的Option设置
            });
            break;
        }
        // case 'polygon':{
        //     mouseTool.polygon({
        //         fillColor:'#00b0ff',
        //         strokeColor:'#80d8ff'
        //         //同Polygon的Option设置
        //     });
        //     break;
        // }
        // case 'rectangle':{
        //     mouseTool.rectangle({
        //         fillColor:'#00b0ff',
        //         strokeColor:'#80d8ff'
        //         //同Polygon的Option设置
        //     });
        //     break;
        // }
        // case 'circle':{
        //     mouseTool.circle({
        //         fillColor:'#00b0ff',
        //         strokeColor:'#80d8ff'
        //         //同Circle的Option设置
        //     });
        //     break;
        // }
    }
}


//为地图注册click事件获取鼠标点击出的经纬度坐标
map.on('click', function (e) {
    document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat()
});

//根据ip精确定位
var options = {
    'showButton': true,//是否显示定位按钮
    'buttonPosition': 'RB',//定位按钮的位置
    /* LT LB RT RB */
    'buttonOffset': new AMap.Pixel(10, 20),//定位按钮距离对应角落的距离
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
    geolocation.getCurrentPosition()
});

/*公交站点查询*/
var markers = [];
// stationSearch();

// function stationSearch() {
//     // 移除原有marker
//     map.remove(markers);
//     markers = [];
//     var stationKeyWord = document.getElementById('tipinput').value;
//     if (!stationKeyWord) return;
//     //实例化公交站点查询类
//     var station = new AMap.StationSearch({
//         pageIndex: 1, //页码
//         pageSize: 60, //单页显示结果条数
//         city: '0592'   //确定搜索城市
//     });
//     station.search(stationKeyWord, function (status, result) {
//         if (status === 'complete' && result.info === 'OK') {
//             stationSearch_CallBack(result);
//         } else {
//             document.getElementById('tip').innerHTML = JSON.stringify(result);
//         }
//     });
// //从本地获取公交站点数据
//     $.ajax({
//         url: "",
//         type: "post",
//         data: null,
//         dataType: "json",
//         success: function (date) {
//
//         },
//     })
//
// }

/*公交站点查询返回数据解析*/
function stationSearch_CallBack(searchResult) {

    var Busline = new Object();
    Busline.id = "1";
    Busline.name = "文辉线";
    Busline.location = new AMap.LngLat(118.180401, 24.493483);
    Busline.start_stop = "王杰站";
    Busline.end_stop = "jj站";
    var buslins = new Array();
    buslins.push(Busline);
    var StationInfo = new Object();
    StationInfo.id = "1";
    StationInfo.name = "文辉站";
    StationInfo.location = new AMap.LngLat(118.180401, 24.493483);
    StationInfo.adcode = "350203";
    StationInfo.citycode = "0592";
    StationInfo.buslines = buslins;
    var stationArr = new Array();
    stationArr.push(StationInfo);
    // var stationArr = searchResult.stationInfo;
    var searchNum = stationArr.length;
    if (searchNum > 0) {
        document.getElementById('tip').innerHTML = '查询结果：共' + searchNum + '个相关站点';
        for (var i = 0; i < searchNum; i++) {
            var marker = new AMap.Marker({
                icon: new AMap.Icon({
                    image: '//a.amap.com/jsapi_demos/static/resource/img/pin.png',
                    size: new AMap.Size(32, 32),
                    imageSize: new AMap.Size(32, 32)
                }),
                offset: new AMap.Pixel(-16, -32),
                position: stationArr[i].location,
                map: map,
                title: stationArr[i].name
            });
            marker.info = new AMap.InfoWindow({
                content: stationArr[i].name,
                offset: new AMap.Pixel(0, -30)
            });
            marker.on('mouseover', function (e) {
                e.target.info.open(map, e.target.getPosition())
            });
            markers.push(marker);
        }
        map.setFitView();
    }
}

// document.getElementById('search').onclick = stationSearch;


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
                    cityArea.append('<li onclick="chooseCity(this)">'+cityList[key].cityName+'</li>')
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
                cityArea.append('<li onclick="chooseCity(this)">'+cityList[key].cityName+'</li>')
        }
    }
}
//点击城市事件
function chooseCity(cityName){
    $(".layui-field-title>legend").text("当前城市："+cityName);
    AMap.plugin(['AMap.PlaceSearch'], function(){
        var placeSearch = new AMap.PlaceSearch({
            map: map
        });  //构造地点查询类
        for (var key in cityList) {
            if (cityList[key].cityName==cityName){
                alert("我来了")
                placeSearch.setCity(cityList[key].adcode);
            }
        }
    });
    //改变天气变化
    $(".city-title>b").text(cityName);
    layer.closeAll();
}