layui.use('table', function () {
    var table = layui.table;
    var $ = layui.$ //重点处
    //方法级渲染
    table.render({

        elem: '#admin'
        , url: '/Admin/getAdminTable'
        , title: '账户管理表'
        , totalRow: true
        , cols: [
            [
                {checkbox: true, fixed: 'left'}
                , {field: 'adminId', width: 50, title: 'ID'}
                , {field: 'account', width: 150, title: '账号'}
                , {
                field: 'roleId', width: 150, title: '角色',
                edit: 'text', sort: true, templet: function (res) {
                    if (res.roleId == 3) {
                        return "调度员";
                    } else if (res.roleId == 4) {
                        return "经理";
                    } else {
                        return "客服人员";
                    }
                },
            }
                , {
                field: 'adminState', width: 80, title: '状态',
                edit: 'text', sort: true, templet: function (res) {
                    if (res.adminState == 4) {
                        return "禁用";
                    } else {
                        return "启用";
                    }
                },
            }
                , {field: 'regTime', width: 200, title: '注册时间'}
                , {field: 'adminName', width: 120, title: '姓名'}
                , {field: 'right', title: '操作', toolbar: '#barDemo'}
            ]
        ]
        , id: 'testReload'
        , page: true
        , height: 310
        , limit: 5
        , limits:[5,10]
    });

    var $ = layui.$, active = {
        reload: function () {
            var adminName = $('#adminName');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    adminName: adminName.val(),
                }
            }, 'data');
        }
    };

    $('.demoTable .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //监听行工具事件
    table.on('tool(admin)', function (obj) {
        var data = obj.data;
        if (obj.event === 'delete') {
            layer.confirm('您真的要删除人家吗？', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/Admin/deleteAdmin",
                    data: {"adminId": data.adminId},
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
        } else if (obj.event === 'update') {
            //修改
            $("#adminId").val(data.adminId);
            $("#account").val(data.account);
            $("#role").val(data.role);
            $("#updateName").val(data.updateName);
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
            if(stateChinese=='启用'){
                state=5;
            }else {
                state=4;
            }
            layer.confirm('您真的要这样做么', function (index) {
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    dataType: "text",
                    url:"/Admin/updateState",
                    data: JSON.stringify({adminId:data.adminId,adminState:state}),
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
            var roleId;
            var adminId = $("#adminId").val();
            var roleName = $("input[name='role']:checked").val();
            var updateName = $("#updateName").val();
            if (roleName == null || roleName == '' || updateName == null || updateName == '') {
                return;
            }
            if (roleName == '调度员') {
                roleId = 3;
            } else if (roleName == '经理') {
                roleId = 4;
            } else {
                roleId = 5;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "adminId": adminId,
                    "roleId": roleId,
                    "adminName": updateName,
                });
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/Admin/updateAdmin",
                data: jsonStr,
                success: function (data) {
                    layer.msg(data);
                    if (data == "修改成功") {
                        layer.msg('修改成功', {
                            time: 3000,
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

var yingwen = /^[A-Z][1]$/;
var num = /^[0-9][6]$/;


//新增人员
layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var role;
        var account = $("#addAcount").val();
        var password = $("#password").val();
        var pass = $("#pass").val();
        var newroleName = $("input[name='newRole']:checked").val();
        var adminName = $("#addName").val();
        if (account == null || account == '' || adminName == null || adminName == ''||password == null || password == '' || pass == null || pass == '') {

            return;
        }
        if (password !== pass) {
            layer.msg("两次输入的密码不一致", {
                "icon": 2,
                "time": 2000
            });
            $("#pass").val("");
            return;
        }
        if (newroleName == '调度员') {
            role = 3;
        } else if (newroleName == '经理') {
            role = 4;
        } else {
            role = 5;
        }

        //获取表单数据
        var jsonStr = JSON.stringify(
            {
                "account": account,
                "password": password,
                "roleId": role,
                "adminName": adminName,

            });
        //向后台发送数据
        $.ajax({
            contentType: "application/json",
            dataType: "text",
            type: "post",
            url: "/Admin/addAdmin",//数据接口，到新增方法的那个路径上
            data: jsonStr,
            success: function (data) {
                layer.msg(data);
                if (data == "添加成功") {
                    layer.msg('添加成功', {
                        time: 3000, //20s后自动关闭
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