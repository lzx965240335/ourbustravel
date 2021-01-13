
$(function () {
    layui.use('form', function(){
        var form = layui.form;
        form.on('submit(formDemo1)', function(data){
            console.log(data);
            $.ajax({
                contentType:"application/json",
                url:"/userMain/login",
                type:"post",
                data:JSON.stringify(data.field),
                dataType:"json",
                success:function (data) {
                    // alert(data)
                    if (data>'0'){
                        // location.href="/main/qt";
                        chat();

                    }else{
                        layer.msg("账号或者密码错误")
                    }
                },
                error:function () {
                    alert('网络繁忙');
                },
            });
            return false;
        });
        form.on('submit(formDemo2)', function(data){
            console.log(data);
            $.ajax({
                url:"/userMain/codeLogin",
                type:"post",
                data:"phoneNum="+data.field.phoneNum+"&verificationCode="+data.field.verificationCode,
                dataType:"json",
                success:function (data) {
                    alert(data)
                    if (data>'0'){
                        // location.href="/userMain/qt";
                    }else{
                        layer.msg("手机号或者验证码错误")
                    }
                },
                error:function () {
                    alert('网络繁忙');
                },
            })
            return false;
        });
    })
})

function chat() {
    $.ajax({
        contentType:"application/json",
        url:"/userMain/getLoginner",
        type:"post",
        data:"",
        dataType:"json",
        success:function (data) {
            console.log(data)
            layui.use('layim', function(layim){
                var layim = layui.layim;
                var socket = new WebSocket('ws://localhost:8080/websocket/'+data.userName);
                socket.onopen = function () {
                    console.log(data.userName+"连接成功")
                    // socket.send('XXX连接成功');
                }
                socket.onmessage = function(res){
                    res = JSON.parse(res.data);
                    console.log(res)
                    layim.getMessage(res
                    )
                };
                if (data.userName=="xx"){
                    layim.config({
                        init: {
                            url: '/json/getList1.json'
                            ,data: {}
                        }

                        //查看群员接口
                        ,members: {
                            url: '/json/getMembers.json'
                            ,data: {}
                        }
                    });
                }else {
                    layim.config({
                        init: {
                            url: '/json/getList.json'
                            ,data: {}
                        }
                        //查看群员接口
                        ,members: {
                            url: '/json/getMembers.json'
                            ,data: {}
                        }
                    });
                }
                layim.on('sendMessage', function(res){
                    var mine = res.mine;
                    var to = res.to; //对方的信息
                    //监听到上述消息后，就可以轻松地发送socket了，如：
                    socket.send(JSON.stringify({
                        type: 'friendMessage' //随便定义，用于在服务端区分消息类型
                        ,data: res
                        ,role : $("#userName").val()
                    }));
                });
            });
        },
        error:function () {
            alert('网络繁忙');
        },
    })
}