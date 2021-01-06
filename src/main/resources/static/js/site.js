layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/site/getSites'
        , title: '站点数据表'
        , toolbar: '#toolbarDemo'
        , totalRow: true
        , cols: [
            [
                {type: 'numbers'},
                {checkbox: true}
                //field后面的值必须跟实体类的属性一致
                // , {field: 'siteId', title: 'ID', sort: true}
                , {field: 'siteName', title: '站点名称'}
                , {field: 'siteX', title: '站点横坐标'}
                , {field: 'siteY', title: '站点纵坐标'}
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
            $("#updateSiteX").val(data.siteX);
            $("#updateSiteY").val(data.siteY);
            $("#updatePeopleNum").val(data.peopleNum);
            console.log("点击修改按钮时" + data);
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
            var updateSiteX = $("#updateSiteX").val();
            var updateSiteY = $("#updateSiteY").val();
            var updatePeopleNum = $("#updatePeopleNum").val();
            if (updateSiteName == null || updateSiteName == '' || updateSiteX == null || updateSiteX == ''
                || updateSiteY == null || updateSiteY == '' || updatePeopleNum == null || updatePeopleNum == '') {
                return;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "siteId": $("#siteId").val(),
                    "siteName": $("#updateSiteName").val(),
                    "siteX": $("#updateSiteX").val(),
                    "siteY": $("#updateSiteY").val(),
                    "peopleNum": $("#updatePeopleNum").val(),
                });
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/site/updateSite",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    if (data == "修改成功") {
                        layer.closeAll();
                    }
                    //数据刷新
                    table.reload('testReload', {}, 'data');
                }
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

// var shuzi = /^[1-9][3]\d*$/
layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var addSiteName = $("#addSiteName").val();
        var addSiteX = $("#addSiteX").val();
        var addSiteY = $("#addSiteY").val();
        var addPeopleNum = $("#addPeopleNum").val();
        // if(!shuzi.test(addPeopleNum)){
        //     alert(1);
        //     return;
        // }

        if (addSiteName == null || addSiteName == '' || addSiteX == null || addSiteX == ''
            || addSiteY == null || addSiteY == '' || addPeopleNum == null || addPeopleNum == '') {
            return;
        }
        //获取表单数据
        var jsonStr = JSON.stringify({
            "siteName": $("#addSiteName").val(),
            "siteX": $("#addSiteX").val(),
            "siteY": $("#addSiteY").val(),
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
