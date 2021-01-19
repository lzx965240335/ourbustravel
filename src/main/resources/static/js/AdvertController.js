layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/Advert/getSelectTable'
        , toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
        , title: '广告合作表'
        , cols: [
            [
                {type: 'checkbox', fixed: 'left'}
                , {title: '序号',type: 'numbers'}
                , {field: 'advertTitle', title: '广告标题',  sort: true}
                , {field: 'begTime', title: '发布时间',  sort: true}
                , {field: 'endTime', title: '结束时间',  sort: true,}
                , {field: 'advertState', title: '状态',  sort: true, templet: function (res) {
                    if (res.dic.dicValue == "" || res.dic.dicValue == null) {
                        return "";
                    } else {
                        return res.dic.dicValue;
                    }
                }
            }, {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 400}
            ]
        ]
        , page: true
        ,id:'testReload'
        ,
    });

    var $ = layui.$, active = {
        reload: function () {
            var beginTime = $('#beginTime');
            var endingTime = $('#endingTime');
            var selState = $("#selState option:selected").val();
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: $(".layui-laypage-skip").find("input").val()
                }
                , where: {
                    beginTime: beginTime.val(),
                    endingTime: endingTime.val(),
                    selState: $("#selState option:selected").val(),
                }
            }, 'data');
        }
    }

    //搜索按钮事件
    $('#search_state').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });



    //监听事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        console.log(obj)
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/Advert/getDelete",
                    data: {"advertId": data.advertId},
                    success: function (data) {
                        layer.msg(data);
                        if (data == "删除成功") {
                            obj.del();
                            layer.close(index);
                        }
                    }
                })
            });
            //监听修改事件
        }else if (obj.event === 'edit'){
            $("#advertId").val(data.advertId);
            $("#resetTitle").val(data.advertTitle);
            $("#resetBegTime").val(data.begTime);
            $("#resetEndTime").val(data.endTime);
            $("#resetAdvertMoney").val(data.advertMoney);
            if(data.advertState==5){
                $("#resetAdvertState").val("启用");
            }else{
                $("#resetAdvertState").val("禁用");
            }
            layer.open({
                type: 1,
                title: ["修改广告"],
                area: ['100%', '100%'],
                content: $("#resetMyInf"),
                cancel: function () {
                },
                success: function (layer) {
                    var btn = $("#resetAffirm");
                }
            })
            //广告信息
        }else if (obj.event === 'detail'){
            // $("#advertIds").val(data.advertId);
            console.log(data.advertUrl);
            $("#iframe").attr("src",data.advertUrl)
            layer.open({
                type: 1,
                title: ["广告信息"],
                area: ['80%', '80%'],
                content: $("#MyInfs"),
            })
        }else if (obj.event === 'enable'){
            var stateChinese=$(obj.tr[2].cells[0].childNodes[0].childNodes[7]).text();
            var state;
            if(stateChinese=='启用'){
               state=5;
            }else {
               state=4;
            }
            layer.confirm('真的要这样做么', function (index) {
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    dataType: "text",
                    url: "/Advert/changeState",
                    data: JSON.stringify({advertId:data.advertId,advertState:state}),
                    success: function (data) {
                        if (data == 'success') {
                            layer.open({
                                type: 1
                                , content: '<div style="padding: 20px 100px;">修改成功</div>'
                                , btn: '关闭全部'
                                , btnAlign: 'c' //按钮居中
                                , shade: 0 //不显示遮罩
                                , yes: function () {
                                    layer.closeAll();
                                }
                            });
                                    //执行重载
                                    table.reload('testReload', {
                                        page: {
                                            curr: $(".layui-laypage-skip").find("input").val()
                                        }
                                        , where: {

                                        }
                                    }, 'data');
                                }
                        }
                })
                layer.close(index);
            })

        }

    });

    //监听新增广告事件
    layui.use(['form','layedit','laydate','layer','table'],function () {
        var form = layui.form
            , layer = layui.layer
            , table = layui.table;
        $("#register").click(function () {
            var title = $("#title").val();
            var begTime = $("#begTime").val();
            var endTime = $("#endTime").val();
            var advertMoney = $("#advertMoney").val();
            var advertState = $("#advertState").val();
            if (title == null || title == '' ||
                begTime == null || begTime == '' ||
                endTime == null || endTime == '' ||
                advertMoney == null || advertMoney == '' ||
                advertState == null || advertState == '' ) {
                return;
            }
            var jsonStr = JSON.stringify({
                "advertTitle": $("#title").val(),
                "begTime": $("#begTime").val(), "endTime": $("#endTime").val(),
                "advertMoney": $("#advertMoney").val(), "advertState": $("#advertState").val()
            });
            $.ajax({
                contentType: "application/json",
                type: "post",
                dataType: "JSON",
                url: "/Advert/getAdd",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    if (data == "新增成功") {
                        layer.closeAll();
                        table.reload('testReload', {}, 'data');
                    }
                }
            })
        })
    })

    //监听修改广告事件
    layui.use(['form','layedit','laydate','layer','table'],function () {
        var form = layui.form
            , layer = layui.layer
            , table = layui.table;
        $("#resetAffirm").click(function () {
            var advertId = $("#advertId").val();
            var resetTitle = $("#resetTitle").val();
            var resetBegTime = $("#resetBegTime").val();
            var resetEndTime = $("#resetEndTime").val();
            var resetAdvertMoney = $("#resetAdvertMoney").val();
            var resetAdvertState = $("#resetAdvertState").val();
            if (resetTitle == null || resetTitle == '' ||
                resetBegTime == null || resetBegTime == '' ||
                resetEndTime == null || resetEndTime == '' ||
                resetAdvertMoney == null || resetAdvertMoney == '' ||
                resetAdvertState == null || resetAdvertState == '' ) {
                return;
            }
            var jsonStr = JSON.stringify({
                "advertId": $("#advertId").val(),
                "advertTitle": $("#resetTitle").val(),
                "begTime": $("#resetBegTime").val(), "endTime": $("#resetEndTime").val(),
                "advertMoney": $("#resetAdvertMoney").val(), "advertState": $("#resetAdvertState").val()
            });
            $.ajax({
                contentType: "application/json",
                type: "post",
                dataType: "JSON",
                url: "/Advert/getReset",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    if (data == "修改成功") {
                        layer.closeAll();
                        table.reload('testReload', {}, 'data');
                    }
                }
            })
        })
    })

    //监听查看广告事件
    layui.use(['form','layedit','laydate','layer','table'],function () {
        var form = layui.form
            , layer = layui.layer
            , table = layui.table;
        $("#resetAffirm").click(function () {
            var advertIds = $("#advertId").val();
            var advertUrl =  $("#advertUrl").val();
            var jsonStr = JSON.stringify({
                "advertId": $("#advertId").val(),
                "advertUrl": $("#advertUrl").val()
            });
            $.ajax({
                contentType: "application/json",
                type: "post",
                dataType: "JSON",
                url: "/Advert/selAdvertMsg",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    }
            })
        })
    })
});

// 点击新增按钮绑定的事件
$("#addBtn").click(function () {
    layer.open({
        type: 1,
        title: ["新增广告"],
        area: ['100%', '100%'],
        content: $("#myInf"),
        cancel: function () {
        },
        success: function (layer) {
            var btn = $("#register");
        }
    })
    // form.render();
})

//时间日期输入框
layui.use('laydate', function(){
    var laydate = layui.laydate;
    //常规用法
    laydate.render({
        elem: '#beginTime'
    });
});
layui.use('laydate', function(){
    var laydate = layui.laydate;
    //常规用法
    laydate.render({
        elem: '#endingTime'
    });
});