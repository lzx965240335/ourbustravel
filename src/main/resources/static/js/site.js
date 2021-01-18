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
                    console.log(marker)
                    markers.push(marker);
                }
                map.setFitView();
            }
        },
    })
}



layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/site/getSites'
        , title: '站点数据表'
        // , toolbar: '#toolbarDemo'
        , totalRow: true
        , cols: [
            [
                {type: 'numbers',title:'序号'},
                {checkbox: true}
                //field后面的值必须跟实体类的属性一致
                // , {field: 'siteId', title: 'ID', sort: true}
                , {field: 'siteName', title: '站点名称'}
                , {field: 'longitude', title: '站点经度坐标'}
                , {field: 'latitude', title: '站点纬度坐标'}
                , {field: 'peopleNum', title: '站点人数', sort: true}
                , {field: 'setTime', title: '更新时间'}
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
            var siteName = $('#siteName');
            var peopleNum = $('#peopleNum');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    siteName: siteName.val(),
                    peopleNum: peopleNum.val(),
                }
            }, 'data');
        }
    };

    $('#searchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //工具栏事件
    table.on('toolbar(test)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        switch (obj.event) {
            case 'getCheckData':
                var data = checkStatus.data;
                layer.alert(JSON.stringify(data));
                break;
            case 'getCheckLength':
                var data = checkStatus.data;
                layer.msg('选中了：' + data.length + ' 个');
                break;
            case 'isAll':
                layer.msg(checkStatus.isAll ? '全选' : '未全选')
                break;
        }
        ;
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
        }
        $("#sureUpdate").click(function () {
            var siteId = $("#siteId").val();
            var updateSiteName = $("#updateSiteName").val();
            var updateLongitude = $("#updateLongitude").val();
            var updateLatitude = $("#updateLatitude").val();
            var updatePeopleNum = $("#updatePeopleNum").val();
            if (updateSiteName == null || updateSiteName == '' || updateLongitude == null || updateLongitude == ''
                || updateLatitude == null || updateLatitude == '' || updatePeopleNum == null || updatePeopleNum == '') {
                return;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "siteId": $("#siteId").val(),
                    "siteName": $("#updateSiteName").val(),
                    "longitude": $("#updateLongitude").val(),
                    "latitude": $("#updateLatitude").val(),
                    "peopleNum": $("#updatePeopleNum").val(),
                });
            layer.confirm('真的确认修改么', function () {
                //向后台发送数据
                $.ajax({
                    contentType: "application/json",
                    dataType: "text",
                    type: "post",
                    url: "/site/updateSite",
                    data: jsonStr,
                    success: function (data) {
                        if (data == "修改成功") {
                            //关闭整个修改界面
                            layer.closeAll();
                        }
                        //弹出层(显示效果）
                        layer.msg(data);
                        //数据刷新
                        table.reload('testReload', {}, 'data');
                    }
                })
            })
        })
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

var shuzi = /^[1-9][3]\d*$/
layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    // 新增页面里面的提交按钮事件
    $("#register").click(function () {
        var addSiteName = $("#addSiteName").val();
        var addLongitude = $("#addLongitude").val();
        var addLatitude = $("#addLatitude").val();
        var addPeopleNum = $("#addPeopleNum").val();

        if (addSiteName == null || addSiteName == '' || addLongitude == null || addLongitude == ''
            || addLatitude == null || addLatitude == '' || addPeopleNum == null || addPeopleNum == '') {
            return;
        }

        //获取表单数据
        var jsonStr = JSON.stringify({
            "siteName": $("#addSiteName").val(),
            "longitude": $("#addLongitude").val(),
            "latitude": $("#addLatitude").val(),
            "peopleNum": $("#addPeopleNum").val()
        });

        //向后台发送数据
        $.ajax({
            contentType: "application/json",
            dataType: "text",
            type: "post",
            url: "/site/addSite",
            data: jsonStr,
            success: function (data) {
                layer.msg(data);
                if (data == "添加成功") {
                    layer.closeAll();
                }
                //数据刷新
                table.reload('testReload', {}, 'data');
            }
        })
    })

    // 点击新增按钮绑定的事件
    $("#addBtn").click(function () {
        layer.open({
            type: 1,
            title: ["新增站点"],
            area: ['100%', '100%'],
            content: $("#myInf"),
            cancel: function () {
            },
            success: function (layero) {
                $("#register");
            }
        })
        form.render();
    })


})
