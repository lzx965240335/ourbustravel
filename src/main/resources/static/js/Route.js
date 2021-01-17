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
                        position: [data[i].longitude,data[i].latitude],
                        map: map,
                        title: data[i].siteName,
                        siteId:data[i].siteId
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

layui.use(['table','layer'], function () {
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
                , {field: 'startSiteImpl',templet:res=> {
                    return res.startSiteImpl==null?'':res.startSiteImpl.siteName;
                }, title: '起点站'}
                , {field: 'endSiteImpl', title: '终点站' ,templet:res => {
                    return res.endSiteImpl==null?'':res.endSiteImpl.siteName;
                }}
                , {field: 'peopleNum', title: '方向',templet:res=> {return res.rightOrLeft==1?'西':'东';}}
                , {field: 'updateTime', title: '更新时间'}
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
                    url: "/site/deleteSite",
                    data: {"siteId": data.siteId},
                    success: function (data) {
                        layer.msg(data);
                        if (data == "删除成功") {
                            obj.del();
                            layer.close(index);
                        }
                    }
                })
            });
        } else if (obj.event === 'update') {
            $("#siteId").val(data.siteId);
            $("#updateSiteName").val(data.siteName);
            $("#updateLongitude").val(data.longitude);
            $("#updateLatitude").val(data.latitude);
            $("#updatePeopleNum").val(data.peopleNum);
            // console.log("点击修改按钮时" + data);
            // 修改弹出框
            layer.open({
                //layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                type: 1,
                title: "修改城市信息",
                area: ['100%', '100%'],
                content: $("#updateTest"),
                success: function () {
                    $("#register");
                }
            })
        }else if (obj.event === 'select') {
            var routeInf=obj.data.routeInf;
            $.ajax({
                type: "post",
                dataType: "text",
                url: "/site/deleteSite",
                data: {"routeInf": routeInf},
                success: function (data) {
                    layer.msg(data);
                    if (data == "删除成功") {
                        obj.del();
                        layer.close(index);
                    }
                }
            })
        }
    });



    // 点击新增按钮绑定的事件
    $("#addBtn").click(function () {
        layer.open({
            type: 1,
            title: ["新增信息"],
            area: ['80%', '80%'],
            content: $("#myInf"),
            cancel: function () {
            },
            success: function () {
                $("#register");
            }
        })
        form.render();
    })

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

