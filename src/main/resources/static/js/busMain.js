layui.use('table', function(){
        var table = layui.table;
        var $ = layui.$ //重点处
        //方法级渲染
        table.render({
            elem: '#bus'
            ,url: '/busMain/getBusTable'
            ,cols: [
                [

                    //field后面的值必须跟实体类的属性一致
                    ,{field:'busId', title: 'ID'}
                    ,{field:'busName', title: '公交路号'}
                    ,{field:'carNum', title: '公交车牌'}
                    ,{
                     field:'busState', title: '车状态',
                     edit: 'text', sort: true, templet: function (res) {
                        if (res.busState == 16) {
                            return "维修中";
                        } else {
                            return "正常";
                        }
                    },
                     }
                    ,{field:'importTime', title: '引入时间'}
                    ,{
                     field:'onloadState', title: '行车状态',
                    edit: 'text', sort: true, templet: function (res) {
                        if (res.onloadState == 13) {
                            return "停站";
                        } if (res.onloadState == 14) {
                            return "行驶中";
                        } else {
                            return "故障";
                        }
                    },
                     }
                    ,{field:'driver', title: '司机'}
                    , {field: 'right', title: '操作', toolbar: '#barDemo'}
                ]
            ]
            , id: 'testReload'
            , page: true
            , height: 310
            , limit: 5
            ,limits:[5,10]
        });

        var $ = layui.$, active = {
            reload: function(){
                var busName = $('#busName');
                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        busName: busName.val(),
                    }
                }, 'data');
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

    //监听行工具事件
    table.on('tool(bus)', function (obj) {
        var data = obj.data;
        if (obj.event === 'delete') {
            layer.confirm('您真的要删除人家吗？', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/busMain/deleteBus",
                    data: {"busId": data.busId},
                    success: function (data) {
                        if (data == "删除成功") {
                            layer.msg('删除成功', {
                                time: 3000, //20s后自动关闭
                                btn: ['明白了']
                            });
                            // 删除指定tr del()
                            obj.del();
                            // 关闭弹出层
                            layer.close(index);
                        }
                    }
                });
            })
        }else if (obj.event === 'update') {
            //修改
            $("#busId").val(data.busId);
            $("#carNum").val(data.carNum);
            $("#busState").val(data.busState);
            $("#onloadState").val(data.onloadState);
            layer.open({
                type: 1,
                title: "修改信息",
                area: ['60%', '60%'],
                content: $("#updateTest"),
                success: function () {
                    $("#sureUpdate");
                }
            })
        }else if (obj.event === 'enable'){

            var stateChinese=$(obj.tr[0].cells[7].childNodes[0].childNodes[7]).text();
            var state;
            if(stateChinese=='正常'){
                state=17;
            }else {
                state=16;
            }
            layer.confirm('您真的要这样做么', function (index) {
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    dataType: "text",
                    url:"/busMain/updateState",
                    data: JSON.stringify({busId:data.busId,busState:state}),
                    success: function (data) {
                        if (data == 'success') {

                            layer.msg('修改成功', {
                                time: 3000, //20s后自动关闭
                                btn: ['明白了']
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

        //修改数据
        $("#sureUpdate").click(function () {
            var busStateId;
            var onloadStateId;
            var busId = $("#busId").val();
            var busStateName = $("input[name='busState']:checked").val();
            var onloadStateName = $("input[name='onloadState']:checked").val();
            if (busStateName == null || busStateName == '' || onloadStateName == null || onloadStateName == '') {
                return;
            }
            if (onloadStateName == '停站') {
                onloadStateId = 13;
            } else if (onloadStateName == '行驶中') {
                onloadStateId = 14;
            } else {
                onloadStateId = 15;
            }
            if (busStateName == '维修中') {
                busStateId = 16;
            } else {
                busStateId = 17;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "busId": busId,
                    "busState": busStateId,
                    "onloadState": onloadStateId,
                });
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/busMain/updateBus",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    if (data == "修改成功") {
                        layer.msg('修改成功', {
                            time: 3000, //20s后自动关闭
                            btn: ['明白了']
                        });
                        layer.closeAll();
                    }
                    //数据刷新
                    table.reload('testReload',{
                        page:{
                            curr:$(".layui-laypage-em").next().html(),
                        },
                        where:{

                        }
                    });

                }
            })
        })
    })

    });
//新增
layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var addBusState;
        var addOnloadState;
        var addbusName = $("#addbusName").val();
        var addCarNum = $("#addCarNum").val();
        var addBusStateName = $("input[name='addBusState']:checked").val();
        var addOnloadStateName = $("input[name='addOnloadState']:checked").val();
        if (addbusName == null || addbusName == '' || addCarNum == null || addCarNum == '' ) {

            return;
        }
        if (addOnloadStateName == '停站') {
            addOnloadState = 13;
        } else if (addOnloadStateName == '行驶中') {
            addOnloadState = 14;
        } else {
            addOnloadState = 15;
        }
        if (addBusStateName == '维修中') {
            addBusState = 16;
        } else {
            addBusState = 17;
        }
        //获取表单数据
        var jsonStr = JSON.stringify(
            {
                "busName": addbusName,
                "carNum": addCarNum,
                "busState": addBusState,
                "onloadState": addOnloadState,

            });
        //向后台发送数据
        $.ajax({
            contentType: "application/json",
            dataType: "text",
            type: "post",
            url: "/busMain/addBus",//数据接口，到新增方法的那个路径上
            data: jsonStr,
            success: function (data) {
                layer.msg(data);
                if (data == "添加成功") {
                    layer.msg('添加成功', {
                        time: 3000,
                        btn: ['明白了']
                    });
                    layer.closeAll();
                    //数据刷新
                    table.reload('testReload',{
                        page:1,
                    });
                }

            }
        })
    })

    // 点击新增按钮绑定的事件
    $("#addBtn").click(function () {
        layer.open({
            type: 1,
            title: ["新增信息"],
            area: ['60%', '60%'],
            content: $("#myInf"),
            cancel: function () {
            },
            success: function () {
                $("#register");
            }
        })
        form.render();
    })


})