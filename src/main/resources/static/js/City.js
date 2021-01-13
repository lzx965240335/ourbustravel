// 中文
var zhongwen = /^[\u4e00-\u9fa5]*$/
// 英文
var yingwen = /^[A-Z]{1}$/
// 六位数字(开头不能为0)
var sixNum = /^\d{6}$/
// 四位数字(开头可以为0)
var fourNum = /^[1-9]d*|0$/
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
        , limits: [5, 10]
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

    // 搜索事件
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
        //获取事件名
        var eventName = obj.event;
        //判断事件名，执行对应的方法
        if (eventName === 'del') {//删除事件
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
            // 给表单赋值
            $("#cityId").val(data.cityId);
            $("#updateCityName").val(data.cityName);
            $("#updateAdCode").val(data.adcode);
            $("#updateCityCode").val(data.citycode);
            $("#updateInitials").val(data.initials);
            // 显示弹出层（修改页面 div）
            layer.open({
                //layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                type: 1,
                //标题
                title: "修改城市信息",
                //大小
                area: ['100%', '100%'],
                //内容，这里content是一个DOM
                content: $("#updateCityView"),

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

            if (!zhongwen.test(updateCityName)) {
                layer.alert("城市名称只能输入中文", {title: "提示", skin: 'layui-layer-molv'});
                return;
            }
            if (!sixNum.test(Number(updateAdCode))) {
                layer.alert("身份证归属地必须输入六位数字", {title: "提示", skin: 'layui-layer-molv'});
                return;
            }
            if (!fourNum.test(Number(updateCityCode))) {
                layer.alert("城市区号必须输入四位数字", {title: "提示", skin: 'layui-layer-molv'});
                return;
            }
            if (!yingwen.test(updateInitials)) {
                layer.alert("城市首字母必须一位大写字母", {title: "提示", skin: 'layui-layer-molv'});
                return;
            }

            // 判断是否为空或者是否是空字符串
            if (updateCityName == null || updateCityName == '' || updateAdCode == null || updateAdCode == ''
                || updateCityCode == null || updateCityCode == '' || updateInitials == null || updateInitials == '') {
                return;
            }

            //获取表单数据
            var jsonStr = JSON.stringify(
                {
                    "cityId": cityId,
                    "cityName": updateCityName,
                    "adcode": updateAdCode,
                    "citycode": updateCityCode,
                    "initials": updateInitials,
                });
            layer.confirm('真的确认修改么', function () {
                //向后台发送数据
                $.ajax({
                    contentType: "application/json",
                    dataType: "text",
                    type: "post",
                    url: "/city/updateCity",
                    data: jsonStr,
                    success: function (data) {
                        if (data == "修改成功") {
                            layer.closeAll();
                        }
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


layui.use(['form', 'layedit', 'laydate', 'layer', 'table'], function () {
    var form = layui.form
        , layer = layui.layer
        , table = layui.table

    $("#register").click(function () {
        var addCityName = $("#addCityName").val();
        var adCode = $("#adCode").val();
        var addCityCode = $("#addCityCode").val();
        var initials = $("#initials").val();

        if (!zhongwen.test(addCityName)) {
            layer.alert("城市名称只能输入中文", {title: "提示", skin: 'layui-layer-molv'});
            return;
        }
        if (!sixNum.test(Number(adCode))) {
            layer.alert("身份证归属地必须输入六位数字", {title: "提示", skin: 'layui-layer-molv'});
            return;
        }
        if (!fourNum.test(Number(addCityCode))) {
            layer.alert("城市区号必须输入四位数字", {title: "提示", skin: 'layui-layer-molv'});
            return;
        }
        if (!yingwen.test(initials)) {
            layer.alert("城市首字母必须一位大写字母", {title: "提示", skin: 'layui-layer-molv'});
            return;
        }

        if (addCityName == null || addCityName == '' || adCode == null || adCode == ''
            || addCityCode == null || addCityCode == '' || initials == null || initials == '') {
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
                if (data == "添加成功") {
                    layer.closeAll();
                }
                layer.msg(data);
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

    // 取消事件
    $("#cancel").click(function () {
        layer.closeAll();
    })


})
