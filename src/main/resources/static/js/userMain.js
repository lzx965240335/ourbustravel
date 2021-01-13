$(function () {
    layui.use('table', function () {
        var table = layui.table;
        var $ = layui.$ //重点处
        //方法级渲染
        table.render({
            elem: '#user'
            , url: '/userMain/getUserTable'
            , cols: [
                [

                    //field后面的值必须跟实体类的属性一致
                    , {field: 'userId', title: 'ID'}
                    , {field: 'userName', title: '姓名'}
                    , {field: 'userSex', title: '性别'}
                    , {field: 'userAge', title: '年龄'}
                    , {field: 'regTime', title: '注册时间'}
                    , {field: 'phoneNum', title: '手机号'}
                    , {field: 'money', title: '余额'}
                    , {field: 'point', title: '积分'}
                ]
            ]
            , id: 'testReload'
            , page: true
            , height: 310
            , limit: 5
            , limits: [5, 10]
        });

        var $ = layui.$, active = {
            reload: function () {
                var userName = $('#userName');
                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    , where: {
                        userName: userName.val(),
                    }
                }, 'data');
            }
        };

        $('.demoTable .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

    });

})


