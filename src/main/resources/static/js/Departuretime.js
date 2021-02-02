layui.use('table', function () {
    var table = layui.table;
    table.render({
        elem: '#test'
        , url: '/Departuretime/SelectTable'
        , toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
        , title: '发车站点表'
        , cols: [
            [
                {type: 'checkbox', fixed: 'left'}
                , {title: '序号',type: 'numbers'}
                , {field: 'busName', title: '巴士名称',  sort: true}
                , {field: 'departureTime', title: '发车时间',  sort: true}
                , {field: 'afterSite', title: '途径站点',  sort: true,}
                ,{fixed: 'right', title: '操作', toolbar: '#barDemo', width: 400}
            ]
        ]
        , page: true
        ,id:'testReload'
        ,
    });

    var $ = layui.$, active = {
        reload: function () {
            var busName = $('#busName');
            //执行重载
            table.reload('testReload', {
                page: {
                    curr: $(".layui-laypage-skip").find("input").val()
                }
                , where: {
                    busName: busName.val(),
                }
            }, 'data');
        }
    }

    //搜索按钮事件
    $('#search_state').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});