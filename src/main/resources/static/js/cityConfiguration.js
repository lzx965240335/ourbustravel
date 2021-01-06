layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/city/getCities'
        , title: '城市数据表'
        , toolbar: '#toolbarDemo'
        , totalRow: true
        , cols: [
            [
                {type: 'numbers'},
                {checkbox: true}
                //field后面的值必须跟实体类的属性一致
                // , {field: 'cityId', title: 'ID', sort: true}
                , {field: 'cityName', title: '城市名称'}
                , {field: 'adcode', title: '身份证归属地'}
                , {field: 'citycode', title: '城市区号'}
                , {field: 'initials', title: '城市首字母', sort: true}
                , {field: 'updateTime', title: '更新时间', sort: true}
                // 设置表头工具栏
                , {field: 'operation', title: '操作', toolbar: '#barDemo'}
            ]
        ]
        , id: 'testReload'
        , page: true
        , limit: 5
        ,limits:[5,10]
    });

    var $ = layui.$, active = {
        reload: function () {
            var cityName = $('#cityName');
            var cityCode = $('#cityCode');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    cityName: cityName.val(),
                    citycode: cityCode.val(),
                }
            }, 'data');
        }
    };

    $('#searchBtn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    // 工具栏事件
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
        //得到当前行的相关信息
        var data = obj.data;
        var eventName = obj.event;
        // 判断事件名，执行对应的方法
        if (eventName === 'del') {//删除
            layer.confirm('真的删除行么', function (index) {//index当前弹出层的下标
                $.ajax({
                    type: "post",
                    dataType: "text",
                    url: "/city/deleteCity",
                    data: {"cityId": data.cityId},
                    success: function (data) {
                        layer.msg(data);
                        if (data == "删除成功") {
                            // 删除指定tr del()
                            obj.del();
                            // 关闭弹出层
                            layer.close(index);
                        }
                    }
                })
            });
        } else if (eventName === 'update') {//修改事件
            $("#cityId").val(data.cityId);
            $("#updateCityName").val(data.cityName);
            $("#updateAdCode").val(data.adcode);
            $("#updateCityCode").val(data.citycode);
            $("#updateInitials").val(data.initials);
            console.log("点击修改按钮时"+data);
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
            var cityId = $("#cityId").val();
            var updateCityName = $("#updateCityName").val();
            var updateAdCode = $("#updateAdCode").val();
            var updateCityCode = $("#updateCityCode").val();
            var updateInitials = $("#updateInitials").val();
            if (updateCityName == null || updateCityName == '' || updateAdCode == null || updateAdCode == ''
                ||updateCityCode == null || updateCityCode == '' || updateInitials == null || updateInitials == '') {
                return;
            }
            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "cityId":$("#cityId").val(),
                    "cityName": $("#updateCityName").val(),
                    "adcode": $("#updateAdCode").val(),
                    "citycode": $("#updateCityCode").val(),
                    "initials": $("#updateInitials").val(),
                });
            //向后台发送数据
            $.ajax({
                contentType: "application/json",
                dataType: "text",
                type: "post",
                url: "/city/updateCity",
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

var yingwen = /^[A-Z][1]$/;
var num=/^[0-9][6]$/;

layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var addCityName = $("#addCityName").val();
        var adCode = $("#adCode").val();
        var addCityCode = $("#addCityCode").val();
        var initials = $("#initials").val();
        // if(!yingwen.test(initials)){
        //     layer.alert("城市首字母只能使用英文仅一位", {  title: "提示",skin: 'layui-layer-molv' });
        //     return false;
        // }
        // if(!num.test(adCode)){
        //     layer.alert("城市首字母仅一位", {  title: "提示",skin: 'layui-layer-molv' });
        //     return false;
        // }

        if (addCityName == null || addCityName == '' || adCode == null || adCode == ''
            ||addCityCode == null || addCityCode == '' || initials == null || initials == '') {
            return;
        }
        //获取表单数据
        var jsonStr = JSON.stringify(
            {
                "cityName": $("#addCityName").val(),
                "adcode": $("#adCode").val(),
                "citycode": $("#addCityCode").val(),
                "initials": $("#initials").val()
            });
        //向后台发送数据
        $.ajax({
            contentType: "application/json",
            dataType: "text",
            type: "post",
            url: "/city/addCity",//数据接口，到新增方法的那个路径上
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
            title: ["新增城市信息"],
            area: ['100%', '100%'],
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
