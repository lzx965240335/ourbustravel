layui.use('element', function () {
    var element = layui.element;
});
$(function () {
    var myTab = document.getElementById("login-type"); //整个div
    var myUl = myTab.getElementsByTagName("ul")[0]; //一个节点
    var myLi = myUl.getElementsByTagName("li"); //数组
    var myDiv = myTab.querySelectorAll('#login-type > div'); //数组
    for (var i = 0; i < myLi.length; i++) {
        myLi[i].index = i;
        myLi[i].onclick = function () {
            for (var j = 0; j < myLi.length; j++) {
                myLi[j].className = "off";
                myDiv[j].className = "hide";

            }
            this.className = "on";
            myDiv[this.index].className = "show";
        }
    }
})
$(function () {
    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate;

            $("#login").click(function () {
                if ($("#login").text()=="登录") {
                layer.open({
                    type: 1,
                    title: null,
                    shade: 0,
                    area: ['580px', '300px'],
                    offset: '300px',
                    content: $("#loginView"),
                    cancel: function () {
                        $(this).css("display", "none")
                    },
                    success: function () {
                        layui.use('form', function () {
                            var form = layui.form;
                            form.on('submit(formDemo1)', function (data) {
                                console.log(data);
                                login(data)
                                return false;
                            });
                            form.on('submit(formDemo2)', function (data) {
                                console.log(data);
                                $.ajax({
                                    url: "/userMain/codeLogin",
                                    type: "post",
                                    data: "phoneNum=" + data.field.phoneNum + "&verificationCode=" + data.field.verificationCode,
                                    dataType: "json",
                                    success: function (data) {
                                        if (data > '0') {
                                            // location.href="/userMain/qt";
                                            location.href="/userMain/map";
                                        } else {
                                            layer.msg("手机号或者验证码错误")
                                        }
                                    },
                                    error: function () {
                                        alert('网络繁忙');
                                    },
                                })
                                return false;
                            });
                        })
                    }
                })
                form.render();
                }
            })
        $(".yzmbtn").click(function () {
            console.log($("#phoneNum").val())
            $.ajax({
                url: "/userMain/verification",
                type: "post",
                data: "phoneNum=" + $("#phoneNum").val(),
                datatype: "text",
                success: function (res) {
                    layer.msg(res)
                }
            })
        })
        function exit() {
            var index =layer.confirm("请确认退出",{
                btn: ['退出', '取消']
            }, function () {
                // 按钮1的事件
                $.ajax({
                    url: "/userMain/userExit",
                    type: "post",
                    data: '',
                    datatype: "text",
                    success: function (data) {
                        layer.msg(data);
                        location.href="/page/whatPage?pageName=map";
                    }
                })
                layer.close(index)
            }, function(){
                // 按钮2的事件
                layer.close(index)
            });

        }
        if ($("#userName").html()){
            console.log($("#avatar").text())
            $(".layui-layer").css("display", "none")
            $(".login-btn").html("退出")
            $(".login-btn").attr('id','exit')
            $("#p1").html("您好："+$('#userName').text())
            $("#p2").html("欢迎回来")
            $(".avatar").css("background","url("+$("#avatar").text()+") 50% no-repeat")
            $("dd a").removeClass('hide')
            document.getElementById('exit').addEventListener('click',function () {
                exit();
            })
        }

        $("#kefu").click(function() {
            if ($(".login-btn").html()=="退出"){
                $.ajax({
                    contentType:"application/json",
                    url:"/userMain/getLoginner",
                    type:"post",
                    data:"",
                    dataType:"json",
                    success:function (data) {
                        layui.use('layim', function(layim){
                            var layim = layui.layim;
                            var socket = new WebSocket('ws://localhost:8080/websocket/'+data.account);
                            socket.onopen = function () {
                                console.log(data.userName+"连接成功");
                                $.ajax({
                                    url: "/userMain/chatLoad",
                                    type: "post",
                                    data: "userId="+data.userId,
                                    datatype: "text",
                                    success: function (res) {
                                        res=JSON.parse(res);
                                        console.log(res.length)
                                        for (let i = 0; i < res.length ; i++) {
                                            // var chatInf ={'msgcontent':res[i].msgcontent,'msgtype':'text'};
                                            var chatInf ={
                                                content:res[i].msgcontent,
                                                id: 101,
                                                name: "在线客服",
                                                timestamp: res[i].sendTime,
                                                type: "friend",
                                                username: data.account
                                            }
                                            layim.getMessage(chatInf
                                            )
                                        }
                                    }
                                })
                            }
                            socket.onmessage = function(res){
                                console.log(res)
                                res=JSON.parse(res.data);
                                var adminId =res['id'];
                                if (res.username=="admin"){
                                    res['name']="在线客服";
                                    res['username']="在线客服";
                                    res['avatar']="http://tp1.sinaimg.cn/1571889140/180/40030060651/1"
                                    res['id']='101';
                                }
                                console.log(res);
                                console.log(data['userId']+"====="+adminId)
                                layim.getMessage(res
                                );
                                chatLoadOne(data['userId'],adminId);
                            };
                            layim.config({
                                init: {
                                    url: '/userMain/userChat'
                                    ,data: {}
                                }

                                //查看群员接口
                                ,members: {
                                    url: '/json/getMembers.json'
                                    ,data: {}
                                }
                            });
                            layim.on('sendMessage', function(res){
                                var mine = res.mine;
                                var to = res.to; //对方的信息
                                //监听到上述消息后，就可以轻松地发送socket了，如：
                                socket.send(JSON.stringify({
                                    type: 'friendMessage' //随便定义，用于在服务端区分消息类型
                                    ,data: res
                                    ,role : 'user'
                                }));
                            });
                        });
                    },
                    error:function () {
                        alert('网络繁忙');
                    },
                })
            }
        })

        function login(data) {
            $.ajax({
                contentType:"application/json",
                url:"/userMain/login",
                type:"post",
                data:JSON.stringify(data.field),
                dataType:"json",
                success:function (data) {
                    // alert(data)
                    if (data>'0'){
                        location.href="/userMain/map";
                    }else{
                        layer.msg("账号或者密码错误")
                    }
                },
                error:function () {
                    alert('网络繁忙');
                },
            });
        }
    });

});

function chatLoadOne(userId,adminId) {
    console.log(userId+"====="+adminId)
    $.ajax({
        url: "/userMain/chatLoadOne",
        type: "post",
        data: "role=A&userId="+userId+"&adminId="+adminId,
        datatype: "text",
        success: function (res) {
        }
    })
}

