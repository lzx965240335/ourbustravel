layui.use('table', function () {
    var table = layui.table;
    var $ = layui.$ //重点处
    //方法级渲染
    table.render({

        elem: '#busWeiXiu'
        , url: '/getBusView/getBusWeiXiu'
        , title: '车辆维修查看'
        , totalRow: true
        , cols: [
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
                    } else if (res.busState == 17) {
                        return "正常";
                    } else {
                        return "故障";
                    }
                },
            }
                ,{field:'sixTime', title: '维修时间'}
                ,{field:'schedule', title: '维修进度'}
                , {field: 'right',  title: '操作', toolbar: '#barDemo'}
            ]
        ]
        , id: 'testReload'
        , page: true
        , height: 310
        , limit: 5
        ,limits:[5,10]
    });
    var $ = layui.$, active = {
        reload: function () {
            var busName = $('#busName');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    busName: busName.val(),
                }
            }, 'data');
        }
    };

    $('.demoTable .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});