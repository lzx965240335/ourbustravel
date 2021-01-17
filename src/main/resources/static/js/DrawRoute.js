var layer;

var clickSites = [null];
var ruler1, ruler2;
//点击的选择按钮
var clickSelectBtn = null;

var route;
$(function () {


});



//显示比例尺
var scale = new AMap.Scale({
        visible: true
    }),
    map = new AMap.Map('container', {
        resizeEnable: true,//是否监控地图容器尺寸变化
        // mapStyle: 'amap://styles/macaron', //设置地图的显示样式
        zoom: 13,//地图显示的缩放级别
        center: [118.178322, 24.492585],//初始化地图中心点
        viewMode: '3D',
        pinch: 45
    });
map.addControl(scale);
//添加3d罗盘
AMap.plugin([
    'AMap.ControlBar',
], function () {
    // 添加 3D 罗盘控制
    map.addControl(new AMap.ControlBar());
});

/*公交站点查询*/
var markers = [];
stationSearch();

/*公交站点查询返回数据解析*/
function stationSearch() {
    // 移除原有marker
    map.remove(markers);
    markers = [];
    $.ajax({
        url: "/site/getSite",
        type: "post",
        data: null,
        dataType: "json",
        success: function (data) {
            var searchNum = data.length;
            if (searchNum > 0) {
                for (var i = 0; i < searchNum; i++) {
                    var marker = new AMap.Marker({
                        icon: new AMap.Icon({
                            image: '//a.amap.com/jsapi_demos/static/resource/img/pin.png',
                            imageSize: new AMap.Size(24, 24)
                        }),
                        offset: new AMap.Pixel(-12, -24),
                        position: [data[i].longitude,data[i].latitude],
                        map: map,
                        title: data[i].siteName,
                        siteId:data[i].siteId
                    });

                    marker.info = new AMap.InfoWindow({
                        content: data[i].siteName,
                        offset: new AMap.Pixel(0, -24)
                    });
                    //点击的监听
                    marker.on('click', function (e) {
                        //如果未点击选择按钮则返回
                        if (clickSelectBtn==null){
                            return;
                        }
                        console.log(e,'id');
                        let inf=e.target._originOpts;
                        let index = $(clickSelectBtn).val();
                        let isExist = false;
                        clickSites.forEach(function (obj) {
                            if (obj != null && obj.title == e.title) {
                                console.log(obj.title);
                                layer.msg(obj.title+'已经存在');
                                isExist = true;
                                //跳出循环
                                return false;
                            }
                        });
                        //如过站点存在则终止
                        if (isExist) {
                            return;
                        }
                        clickSites.splice(index, 1, inf);
                        $(clickSelectBtn).attr('class', 'selectSite');
                        $(clickSelectBtn).text(inf.title);
                    });
                    markers.push(marker);

                }
                map.setFitView();
            }
        },
    })
}

layui.use(['laydate', 'layer'], function () {
    layui.layer;
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

//点击的选择按钮
function clickSiteBtn(e) {
    $(clickSelectBtn).attr('class', 'selectSite');
    if ($(clickSelectBtn).val() == $(e).val()) {
        clickSelectBtn = null;
    } else {
        $(e).attr('class', 'selectSite color');
        clickSelectBtn = e;
    }
}

// 增加按钮
function addSelectBtn() {
    if (clickSelectBtn == null) {
        let value = $('.selectSite').length;
        $('#route').append(
            "<div class=\"row_container\">\n" +
            "                <button type=\"button\" class=\"selectSite\" onclick=\"clickSiteBtn(this)\" value='" + value + "'>选择站点</button>\n" +
            "            </div>"
        );
        clickSites.push(null);
        //在选择节点的下方追加
    } else {
        //重新赋值value
        $('.selectSite').each(function (index, obj) {
            if ($(clickSelectBtn).val()< index) {
                let value = parseInt($(obj).val());
                $(obj).val(value +1)
            }
        });
        $(clickSelectBtn).parent().after(
            "<div class=\"row_container\">\n" +
            "                <button type=\"button\" class=\"selectSite\" onclick=\"clickSiteBtn(this)\" value='" + (parseInt($(clickSelectBtn).val())+1) + "'>选择站点</button>\n" +
            "            </div>"
        );
        clickSites.splice(parseInt($(clickSelectBtn).val())+1, 0, null);
    }

    if (clickSites.length>6){
        $('#myInf').css('height','60%');
    }
}

//删除按钮
function deleteSelectBtn() {

    if (clickSelectBtn == null) {
        layer.msg('请选择站点');
        return;
    }

    if ($(clickSelectBtn).val()==0){
        layer.msg('起始站不能删除');
        return;
    }

    let index= getAlert('确定删除站点吗？',function () {
        //删除这个站点
        clickSites.forEach(function (obj, index) {
            if (index == $(clickSelectBtn).val()) {
                clickSites.splice(index, 1);
            }
        });

        //重新赋值value
        $('.selectSite').each(function (index, obj) {
            if ($(clickSelectBtn).val() < index) {
                let value = parseInt($(obj).val());
                $(obj).val(value - 1)
            }
        });
        console.log(clickSites, '删除后的站点');
        $(clickSelectBtn).parent().remove();
        clickSelectBtn = null;
        if (clickSites.length<6){
            $('#myInf').css('height','');
        }
        layer.msg('删除成功');
        layer.close(index);
    });


}


//重置按钮
function resetSelectBtn(title,session) {
  let index= getAlert('确定重置站点吗？',function () {
        clickSites=[null];
        $('.selectSite').each(function (index, obj) {
            if ($(obj).val()>0) {
                $(obj).parent().remove();
            }else {
                $(obj).text('选择起始站点');
                $('#myInf').css('height','');
            }
        });
      layer.msg('重置成功');
        layer.close(index);
    });
}

//获取弹窗
function getAlert(title,session) {
    return layer.alert(title, {
        skin: 'layui-layer-molv' //样式类名  自定义样式
        ,closeBtn: 1    // 是否显示关闭按钮 0不显示，1  2 样式
        ,anim: 0 //动画类型
        ,btn: ['确定','取消'] //按钮
        ,icon: 6    // icon 5是失败
        ,yes:session
    });
}

//图片是否向上
var isTop=true;
function show() {
    $('#route').slideToggle("slow");
    if (isTop){
        isTop=false;
    }else {
        isTop=true;
        $('#myInf').css('height','');
    }
}
//点击下下一步
function nextBtn(e){
    if (e===0){
        refresh();
    }else {
        $('#myInf').css('display','block');
        $('#drawRoute_container').css('display','none');
        $('.route_view').remove();
    }
}

//刷新线路
function refresh() {
    if (clickSites.length<2){
        layer.msg('至少要有起始站和终点站');
        return;
    }
    $('#myInf').css('display','none');
    $('#drawRoute_container').css('display','block');
    console.log(clickSites)
    //绘制初始路径
    var path = [];
    $(clickSites).each(function (index,obj) {
        if ($(this)!=null){
            path.push(obj.position)
        }
    });
    if (route){
        route.destroy();
    }
    map.plugin("AMap.DragRoute", function() {
        route = new AMap.DragRoute(map,path,AMap.DrivingPolicy.LEAST_DISTANCE); //构造拖拽导航类
        route.search(); //查询导航路径并开启拖拽导航
    });
}



//保存路线
function saveRoute() {
  let index=getAlert('确定保存此路线',function () {
      console.log(route.getRoute());
      let routeInf='';
      let startSite=-1;//起始站
      let endSite;
      $(clickSites).each(function (index,obj) {
          if ($(this)!=null){
              routeInf+=obj.siteId;
              if (index<clickSites.length-1){
                  routeInf+='#'
              }else {
                  endSite=obj.siteId;
              }
              if (startSite===-1){
                  startSite=obj.siteId;
              }

          }
      });

        let addRoute={
            routeInf:routeInf,
            routeName:'yy线',
            startSite:startSite,
            endSite:endSite,
            sites:[]
        };
      $(route.getRoute()).each(function (i,obj) {
          let site={
              longitude:obj.lng,
              latitude:obj.lat,
              isDot:1
          };
          addRoute.sites.push(site);
      });

      $.ajax({
          contentType:"application/json",
          url: "/routeController/addRoute",
          type: "post",
          data: JSON.stringify(addRoute),
          dataType: "json",
          success: function (data) {
              console.log(data);
              if (data){
                  layer.msg('保存成功');
              }
          },
      })

    });
}






