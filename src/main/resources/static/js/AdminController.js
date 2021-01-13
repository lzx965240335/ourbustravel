layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/Admin/getSelectTable'
        , toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
        , defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
            title: '提示'
        }]
        , title: '用户数据表'
        , cols: [
            [
                {type: 'checkbox', fixed: 'left'}
                , {title: '序号',type: 'numbers'}
                , {field: 'adminName', title: '姓名',  sort: true}
                , {field: 'account', title: '账号',  sort: true}
                , {field: 'roleId', title: '角色',  sort: true, templet: function (res) {
                    if (res.role.roleName == "" || res.role.roleName == null) {
                        return "";
                    } else {
                        return res.role.roleName;
                    }
                }
            }
                , {
                field: 'adminState', title: '状态', edit: 'text', sort: true, templet: function (res) {
                    if (res.dic.dicValue == "" || res.dic.dicValue == null) {
                        return "";
                    } else {
                        return res.dic.dicValue;
                    }
                }
            }
                , {field: 'regTime', title: '注册时间',edit: 'text', sort: true}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width: 400}
            ]
        ]
        , page: true
    });

    //头工具栏事件
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
                layer.msg(checkStatus.isAll ? '全选' : '未全选');
                break;

            //自定义头工具栏右侧图标 - 提示
            case 'LAYTABLE_TIPS':
                layer.alert('这是工具栏右侧自定义的一个图标按钮');
                break;
        }

    });

    //监听删除事件
    table.on('tool(test)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('真的删除行么', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/Admin/getDelete",
                    data: {"adminId": data.adminId},
                    success: function (data) {
                        layer.msg(data);
                        if (data == "删除成功") {
                            obj.del();
                            layer.close(index);
                        }
                    }
                })
            });
            //监听重置密码事件
        } else if (obj.event === 'reset') {
            layer.confirm('确认重置密码么,重置密码为123456', function (index) {
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/Admin/getResetPwd",
                    data: {"account": data.account},
                    success: function (data) {
                        layer.msg(data);
                        if (data == "重置密码成功") {
                            obj.update();
                            layer.close(index);
                        }
                    }
                })
            });
        }else if (obj.event === 'amend'){
            $("#adminId").val(data.adminId);
            $("#resetAccount").val(data.account);
            $("#resetPwd").val(data.password);
            $("#resetRole").val(data.roleId);
            $("#resetRoleState").val(data.adminState);
            $("#resetRegTime").val(data.regTime);
            $("#resetAdminName").val(data.adminName);
            layer.open({
                type: 1,
                title: ["修改人员"],
                area: ['100%', '100%'],
                content: $("#resetMyInf"),
                cancel: function () {
                },
                success: function (layer) {
                    var btn = $("#resetAffirm");
                }
            })
        }else if (obj.event === 'jurisdiction'){

        }
});


    //监听新增角色事件
    layui.use(['form','layedit','laydate','layer','table'],function () {
        var form = layui.form
            , layer = layui.layer
            , table = layui.table;
        $("#register").click(function () {
            var account = $("#account").val();
            var pwd = $("#pwd").val();
            var addRole = $("#addRole").val();
            var addRoleState = $("#addRoleState").val();
            var regTime = $("#regTime").val();
            var addAdminName = $("#addAdminName").val();
            if (account == null || account == '' ||
                pwd == null || pwd == '' ||
                addRole == null || addRole == '' ||
                addRoleState == null || addRoleState == '' ||
                regTime == null || regTime == '' ||
                addAdminName == null || addAdminName == '') {
                return;
            }
            var jsonStr = JSON.stringify({
                "account": $("#account").val(),
                "password": $("#pwd").val(), "roleId": $("#addRole").val(),
                "adminState": $("#addRoleState").val(), "regTime": $("#regTime").val(),
                "adminName": $("#addAdminName").val()
            });
            $.ajax({
                contentType: "application/json",
                type: "post",
                dataType: "JSON",
                url: "/Admin/getAdd",
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

    //监听修改角色事件
    layui.use(['form','layedit','laydate','layer','table'],function () {
        var form = layui.form
            , layer = layui.layer
            , table = layui.table;
        $("#resetAffirm").click(function () {
                var adminId = $("#adminId").val();
                var resetAccount = $("#resetAccount").val();
                var resetPwd = $("#resetPwd").val();
                var resetRole = $("#resetRole").val();
                var resetRoleState = $("#resetRoleState").val();
                var resetRegTime = $("#resetRegTime").val();
                var resetAdminName = $("#resetAdminName").val();
                if (resetAccount == null || resetAccount == '' ||
                    resetPwd == null || resetPwd == '' ||
                    resetRole == null || resetRole == '' ||
                    resetRoleState == null || resetRoleState == '' ||
                    resetRegTime == null || resetRegTime == '' ||
                    resetAdminName == null || resetAdminName == '') {
                    return;
                }
                var jsonStr = JSON.stringify({
                    "adminId": $("#adminId").val(),
                    "account": $("#resetAccount").val(),
                    "password": $("#resetPwd").val(), "roleId": $("#resetRole").val(),
                    "adminState": $("#resetRoleState").val(), "regTime": $("#resetRegTime").val(),
                    "adminName": $("#resetAdminName").val()
                });
                $.ajax({
                    contentType: "application/json",
                    type: "post",
                    dataType: "JSON",
                    url: "/Admin/getReset",
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
});
// 点击新增按钮绑定的事件
$("#addBtn").click(function () {
    layer.open({
        type: 1,
        title: ["新增城市"],
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



function jurisdictionCase(node) {
        //ajax请求后台拿对应的data数据
    var getData;
    layer.open({
        type: 1,
        title: ["权限分配"],
        area: ['600px', '500px'],
        content: $("#test1"),
        btn: ['确定'],
        cancel: function () {
        },
        success: function () {
            //权限穿梭框
            layui.use(['transfer', 'layer', 'util'], function() {
                var $ = layui.$
                    , transfer = layui.transfer
                    , layer = layui.layer
                    , util = layui.util;

                //模拟数据
                var data1 = [
                    {"value": "1", "title": "李白"}
                    , {"value": "2", "title": "杜甫"}
                    , {"value": "3", "title": "苏轼"}
                    , {"value": "4", "title": "李清照"}
                    , {"value": "5", "title": "鲁迅"}
                    , {"value": "6", "title": "巴金"}
                    , {"value": "7", "title": "冰心"}
                    , {"value": "8", "title": "矛盾"}
                    , {"value": "9", "title": "贤心"}
                ]

                var jsondata= JSON.stringify(data1);

                //基础效果
                setTimeout(transfer.render({
                    title:['22','223'],
                elem: '#test1'
                    , data: data1
                    ,value:['1','2','3']
                    ,id:'demo1'
                    ,onchange: function(data, index){
                        getData = transfer.getData('demo1');
                        console.log(getData);
                    }
            }), 1000);


            })
        } ,yes: function(){
           //ajax重新對權限分配
            //获得右侧数据


            console.log(getData);

        }
    })


}