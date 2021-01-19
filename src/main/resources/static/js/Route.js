
var route;
//显示比例尺
var scale = new AMap.Scale({
        visible: true
    }),
    map = new AMap.Map('container', {
        resizeEnable: true,//是否监控地图容器尺寸变化
        mapStyle: 'amap://styles/macaron', //设置地图的显示样式
        zoom: 13,//地图显示的缩放级别
        center: [118.178322, 24.492585],//初始化地图中心点
        viewMode: '3D',
        pinch: 45
    });
map.addControl(scale);
//添加3d罗盘
AMap.plugin([
    'AMap.ControlBar',
], function () {
    // 添加 3D 罗盘控制
    map.addControl(new AMap.ControlBar());
});

/*公交站点查询*/
var markers = [];
stationSearch();

/*公交站点查询返回数据解析*/
function stationSearch() {
    // 移除原有marker
    map.remove(markers);
    markers = [];
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
                    marker.on('mouseover', function (e) {
                        e.target.info.open(map, e.target.getPosition())
                    });

                    markers.push(marker);
                }
                map.setFitView();
            }
        },
    })
}

layui.use(['table', 'layer'], function () {
    var table = layui.table, layer = layui.layer;
    table.render({
        elem: '#test'
        , url: '/routeController/routeList'
        , title: '站点数据表'
        , totalRow: true
        , cols: [[
            {type: 'numbers'},
            {checkbox: true}
            //field后面的值必须跟实体类的属性一致
            // , {field: 'siteId', title: 'ID', sort: true}
            , {field: 'routeName', title: '线路名称'}
            , {
                field: 'startSiteImpl', templet: res => {
                    return res.startSiteImpl == null ? '' : res.startSiteImpl.siteName;
                }, title: '起点站'
            }
            , {
                field: 'endSiteImpl', title: '终点站', templet: res => {
                    return res.endSiteImpl == null ? '' : res.endSiteImpl.siteName;
                }
            }
            , {
                field: 'peopleNum', title: '方向', templet: res => {
                    return res.rightOrLeft == 1 ? '正' : '反';
                }
            }
            , {field: 'buildTime', title: '创建时间'}
            , {field: 'operation', title: '操作', toolbar: '#barDemo'}
        ]
        ]
        , id: 'testReload'
        , page: true
        , limit: 5
        , limits: [5, 10]
    });

    var $ = layui.$, active = {
        reload: function () {
            var routeName = $('#routeName');
            var startName = $('#startName');
            var endName = $('#endName');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    routeName: routeName.val(),
                    startName: startName.val(),
                    endName: endName.val(),
                }
            }, 'data');
        }
    };

    $('#searchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/routeController/deleteRoute",
                    data: "routeId=" + obj.data.routeId,
                    success: function (data) {
                        if (data) {
                            layer.msg('删除成功');
                            obj.del();
                            layer.close(index);
                        }else {
                            layer.msg('删除失败');
                        }
                    }
                })
            });
        } else if (obj.event === 'update') {
            window.open('/routeController/updateRoutePage?routeId=' + obj.data.routeId + '');
        } else if (obj.event === 'select') {

            if (route) {
                route.destroy();
            }
            $('.select').text('查看');
            let node = obj.tr[0].lastChild.children[0].children[0];
            if ($(node).text() == '查看') {
                $(node).text('恢复');
                $.ajax({
                    url: "/routeController/updateRoute",
                    type: "post",
                    data: "routeId=" + obj.data.routeId,
                    dataType: "json",
                    success: function (data) {
                        if (data.length < 1) {
                            layer.msg('服务器旧路线查询失败');
                        }
                        let path = [];
                        for (let i = 0; i < data.length; i++) {
                            path.push([data[i].longitude, data[i].latitude]);
                            if (i < data.length - 10) {
                                i += 10;
                            }
                        }
                        map.plugin("AMap.DragRoute", function () {
                            route = new AMap.DragRoute(map, path, AMap.DrivingPolicy.LEAST_DISTANCE); //构造拖拽导航类
                            route.startMarkerOptions.visible = false;
                            route.midMarkerOptions.visible = false;
                            route.endMarkerOptions.visible = false;
                            route.search(); //查询导航路径并开启拖拽导航
                            route.on('addway', function (e) {
                                console.log(e)
                            })
                        });
                    },
                    error: function () {
                        layer.msg('服务器旧路线查询失败')
                    }
                });
            } else {
                route.destroy();
                $(node).text('查看');
            }
        }
    });
});


layui.use('laydate', function () {
    var laydate = layui.laydate;
    lay('.test-item').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
        });
    });
});

layui.use('element', function () {
    var element = layui.element;
});

