layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/news/getNewsInf'
        , title: '新闻数据表'
        , totalRow: true
        , cols: [
            [
                {type: 'numbers', title: "序号"}
                , {checkbox: true}
                // , {field: 'newsId', title: 'ID', sort: true}
                , {field: 'newTitle', title: '新闻标题'}
                , {field: 'newsTime', title: '新闻时间', sort: true}
                , {field: 'newsMsg', title: '新闻内容'}
                , {field: 'newsState', title: '状态', sort: true, templet: function (res) {
                    if (res.newsState == 4) {
                        return "禁用";
                    } else {
                        return "启用";
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
            var selState = $("#selState option:selected");
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    newTile: newTile.val(),
                    startTime: startTime.val(),
                    endTime: endTime.val(),
                    newsState: selState.val(),

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
            $("#updateNewsMsg").val(data.newsMsg);
            layer.open({
                //layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                type: 1,
                title: "修改新闻信息",
                area: ['100%', '100%'],
                content: $("#updateTest"),
                // success: function () {
                // $("#register");
                // }
            })

        } else if (obj.event === 'check') {
            //示范一个公告层
            layer.open({
                type: 1,
                title: false,//不显示标题栏
                closeBtn: false,
                area: '400px;',
                // shade: 0.8,
                id: 'LAY_layuipro', //设定一个id，防止重复弹
                btn: ['关闭'],
                btnAlign: 'c',
                moveType: 1,//拖拽模式，0或者1
                content: '<div style="padding: 50px; text-align: center;line-height: 50px; background-color: #393D49; color: #fff; font-weight: 300;">' + obj.data.newsMsg + '</div>'
            })
        } else if (obj.event === 'state') {
            var stateChinese = $(obj.tr[0].cells[5].childNodes[0].childNodes[5]).text();
            var state;
            if (stateChinese == "启用") {
                state = 5;
            } else {
                state = 4;
            }
            layer.confirm('真的修改状态么', function (index) {
                $.ajax({
                    contentType: "application/json",
                    dataType: "text",
                    type: "post",
                    url: "/news/updateState",
                    data: JSON.stringify({newsId: data.newsId, newsState: state}),
                    success: function (data) {
                        if (data == 'success') {
                            layer.msg("状态修改成功");
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
            var updateNewsMsg = $("#updateNewsMsg").val();
            if (updateNewsTitle == null || updateNewsTitle == '' || updateNewsMsg == null || updateNewsMsg == '') {
                return;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "newsId": newsId,
                    "newTitle": updateNewsTitle,
                    "newsMsg": updateNewsMsg,
                });
            //向后台发送数据
            layer.confirm('真的确认修改么', function () {
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
                        layer.msg(data);
                        //数据刷新
                        table.reload('testReload');
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

// layui.use('element', function () {
//     var element = layui.element;
// });

layui.use(['form', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function (data) {
        var addNewsTitle = $("#addNewsTitle").val();
        var addNewsMsg = $("#addNewsMsg").val();
        if (addNewsTitle == null || addNewsTitle == '' || addNewsMsg == null || addNewsMsg == '') {
            return;
        }
        //获取表单数据
        var jsonStr = JSON.stringify({
            "newTitle": addNewsTitle,
            "newsMsg": addNewsMsg,
        });
        layer.confirm('真的确认提交么', function () {
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/news/addNewsInf",
                data: jsonStr,
                success: function (data) {
                    if (data == "新增成功") {
                        layer.closeAll();
                    }
                    //弹出信息
                    layer.msg(data);
                    //数据刷新
                    table.reload('testReload', {}, 'data');
                }
            })
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

    // 取消事件
    $("#cancel").click(function () {
        layer.closeAll();
    })
})


