layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/news/getNewsInf'
        , title: '新闻数据表'
        , totalRow: true
        , cols: [
            [
                {checkbox: true, fixed: 'left'}
                , {type: 'numbers', title: "序号"}
                //field后面的值必须跟实体类的属性一致
                // , {field: 'newsId', title: 'ID', sort: true}
                , {field: 'newTitle', title: '新闻标题'}
                , {field: 'newsTime', title: '新闻时间', sort: true}
                // , {field: 'newsUrl', title: '新闻地址'}
                , {
                field: 'newsState', title: '状态',
                 sort: true, templet: function (res) {
                    if (res.dicValue == "" || res.dicValue == null) {
                        return "";
                    } else {
                        return res.dicValue;
                    }
                },
            }
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
            var newTile = $('#newsTile');
            var startTime = $('#startTime');
            var endTime = $('#endTime');
           var selState=  $("#selState option:selected").val();
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    newTile: newTile.val(),
                    startTime: startTime.val(),
                    endTime: endTime.val(),
                    selState: $("#selState option:selected").val(),

                }
            }, 'data');
        }
    };

    //搜索事件
    $('#searchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    //监听行工具事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        console.log(obj);
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/news/deleteNews",
                    data: {"newsId": data.newsId},
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
            $("#newsId").val(data.newsId);
            $("#updateNewsTitle").val(data.newTitle);
            console.log("点击修改按钮时" + data);

            layer.open({
                //layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                type: 1,
                title: "修改新闻信息",
                area: ['100%', '100%'],
                content: $("#updateTest"),
                success: function () {
                    $("#register");
                }
            })

        } else if (obj.event === 'check') {


        } else if (obj.event === 'state') {
            var stateChinese = $(obj.tr[0].cells[5].childNodes[0].childNodes[5]).text();
            console.log(stateChinese);
            var state;
            if (stateChinese == "启用") {
                state = 5;
            } else {
                state = 4;
            }
            layer.confirm('真的这么做么', function (index) {
                $.ajax({
                    contentType: "application/json",
                    dataType: "text",
                    type: "post",
                    url: "/news/updateState",
                    data: JSON.stringify({newsId: data.newsId, newsState: state}),
                    success: function (data) {
                        if (data == 'success') {
                            layer.msg("状态修改成功");
                            // layer.open({
                            //     type: 1,
                            //     content: '<div style="padding:20px 100px:">修改成功</div>',
                            //     btn: '关闭全部',
                            //     btnAlign: 'c',
                            //     shade: 0,//不显示遮罩
                            //     yes: function () {
                            //         layer.closeAll();
                            //     }
                            //
                            // });
                        }
                        table.reload('testReload', {
                            page: {
                                curr: $(".layui-laypage-skip").find("input").val()
                            }
                            , where: {}
                        }, 'data');
                    }
                })
                layer.close(index);
            })
        }


        $("#sureUpdate").click(function () {
            var newsId = $("#newsId").val();
            var updateNewsTitle = $("#updateNewsTitle").val();
            if (updateNewsTitle == null || updateNewsTitle == '') {
                return;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "newsId": $("#newsId").val(),
                    "newTitle": $("#updateNewsTitle").val(),
                });
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/news/updateNews",
                data: jsonStr,
                success: function (data) {
                    if (data == "修改成功") {
                        layer.closeAll();
                    }
                    //数据刷新
                    table.reload('testReload');
                    layer.msg(data);
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
layui.use(['form', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var addNewsTitle = $("#addNewsTitle").val();
        if (addNewsTitle == null || addNewsTitle == '') {
            return;
        }
        //获取表单数据
        var jsonStr = JSON.stringify({
            "newTitle": $("#addNewsTitle").val(),
        });
        //向后台发送数据
        $.ajax({
            contentType: "application/json",
            dataType: "text",
            type: "post",
            url: "/news/addNewsInf",
            data: jsonStr,
            success: function (data) {
                layer.msg(data);
                if (data == "新增成功") {
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
            title: ["新增新闻信息"],
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


