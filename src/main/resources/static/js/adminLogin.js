
$(function(){
    setCozyText();
    var data=new Date();
    $("#time").text(data.Format("yyyy/MM/dd hh:mm:ss"));
    //右移动画
    $("#left").animate({left:'0'},1100);

});

function loginBut(){
    var jsonStr=JSON.stringify({'account': $("#account").val(), 'password': $("#password").val()});
    $.ajax({
        contentType:"application/json",
        dataType:"text",
        url:"/Admin/adminInfLogin",
        type:"POST",
        data:jsonStr,
        success:function (data) {
            var isSuccess=6;
            if (data!='登录成功'){
                isSuccess=5;
            }
            layui.use('layer', function(){
                var layer = layui.layer;
                layer.alert(data, {
                    skin: 'layui-layer-molv' //样式类名  自定义样式
                    ,closeBtn: 0    // 是否显示关闭按钮 0不显示，1  2 样式
                    ,anim: 0 //动画类型
                    ,btn: ['确定'] //按钮
                    ,icon: isSuccess    // icon 5是失败
                    ,yes:function(){
                        if (data=='登录成功'){
                            location.href="/hello/initMenu";
                        }

                    }
                });
            });

        }
    });


}


setInterval(function () {
    var data=new Date();
    $("#time").text(data.Format("yyyy/MM/dd hh:mm:ss"));
},1000);

//改变时间提示
setInterval(function () {
    setCozyText();
},3600000);

//改变提示
function setCozyText () {
    var hour=new Date().getHours();
    var msg="上午好!";
    if(hour < 6){msg="凌晨好！"}
    else if (hour < 9){msg="早上好！"}
    else if (hour < 12){msg="上午好！"}
    else if (hour < 14){msg="中午好！"}
    else if (hour < 17){msg="下午好！"}
    else if (hour < 19){msg="傍晚好！"}
    else if (hour < 22){msg="晚上好！"}
    else {msg="深夜了！"}
    $("#cozy").text(msg);
};

//入场动画
function cartoon(){

  $("#lump").css();
};


Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};