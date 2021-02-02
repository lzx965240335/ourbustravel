var layer;

var clickSites = [null];
var ruler1, ruler2;
//点击的选择按钮
var clickSelectBtn = null;

var routeNmae = '';

var isUpdate = 0;//0不是修改页面进来  1是
var route;
$(function () {
    let sites = $('.sites');
    if (sites.length > 1) {
        isUpdate = 1;
        clickSites = [];
        $(sites).each(function (obj) {
            let siteId = $(this)[0].attributes.siteId.nodeValue;
            let longitude = $(this)[0].attributes.longitude.nodeValue;
            let latitude = $(this)[0].attributes.latitude.nodeValue;
            let siteName = $(this)[0].attributes.siteName.nodeValue;
            let site = {
                siteId: siteId,
                position: [longitude, latitude],
                title: siteName,
            };
            clickSites.push(site);
        });
        console.log(clickSites);
        //获取线路名
        routeNmae = $('#routeId')[0].attributes.routeName.nodeValue;
    }
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
                        position: [data[i].longitude, data[i].latitude],
                        map: map,
                        title: data[i].siteName,
                        siteId: data[i].siteId,
                        minDecibels: -1
                    });

                    marker.info = new AMap.InfoWindow({
                        content: data[i].siteName,
                        offset: new AMap.Pixel(0, -24)
                    });
                    marker.on('mouseover', function (e) {
                        e.target.info.open(map, e.target.getPosition())
                    });
                    //点击的监听
                    marker.on('click', function (e) {
                        //如果未点击选择按钮则返回
                        if (clickSelectBtn == null) {
                            return;
                        }
                        console.log(e, 'id');
                        let inf = e.target._originOpts;
                        let index = $(clickSelectBtn).val();
                        let isExist = false;
                        clickSites.forEach(function (obj) {
                            if (obj != null && obj.title ==  e.target.dom.title) {
                                console.log(obj.title);
                                layer.msg(obj.title + '已经存在');
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
    setUpdate(function () {

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
                if ($(clickSelectBtn).val() < index) {
                    let value = parseInt($(obj).val());
                    $(obj).val(value + 1)
                }
            });
            $(clickSelectBtn).parent().after(
                "<div class=\"row_container\">\n" +
                "                <button type=\"button\" class=\"selectSite\" onclick=\"clickSiteBtn(this)\" value='" + (parseInt($(clickSelectBtn).val()) + 1) + "'>选择站点</button>\n" +
                "            </div>"
            );
            clickSites.splice(parseInt($(clickSelectBtn).val()) + 1, 0, null);
        }

        if (clickSites.length > 6) {
            $('#myInf').css('height', '60%');
        }
    }, function () {
        addSelectBtn();
    });

}

//删除按钮
function deleteSelectBtn() {

    if (clickSelectBtn == null) {
        layer.msg('请选择站点');
        return;
    }
    console.log($(clickSelectBtn).val(), '每')
    if ($(clickSelectBtn).val() == 0) {
        layer.msg('起始站不能删除');
        return;
    }
    setUpdate(function () {
        let index = getAlert('确定删除站点吗？', function () {
            //删除这个站点
            $(clickSites).each(function (index, obj) {
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
            if (clickSites.length < 6) {
                $('#myInf').css('height', '');
            }
            layer.msg('删除成功');
            layer.close(index);
        });
    }, function () {
        deleteSelectBtn();
    });


}

//设置修改
function setUpdate(yes, method) {
    if (isUpdate === 0) {
        yes();
    } else {
        let index = getAlert('该操作会重置路线，是否继续', function () {
            isUpdate = 0;
            method();
            layer.close(index)
        })
    }
}


//重置按钮
function resetSelectBtn(title, session) {
    setUpdate(function () {
        let index = getAlert('确定重置站点吗？', function () {
            clickSites = [null];
            $('.selectSite').each(function (index, obj) {
                if ($(obj).val() > 0) {
                    $(obj).parent().remove();
                } else {
                    $(obj).text('选择起始站点');
                    $('#myInf').css('height', '');
                }
            });
            layer.msg('重置成功');
            layer.close(index);
            location.reload();
        });
    }, function () {
        resetSelectBtn();
    });

}

//获取弹窗
function getAlert(msg, session, title) {
    return layer.alert(msg, {
        title: title === undefined ? '信息' : title,
        skin: 'layui-layer-molv' //样式类名  自定义样式
        , closeBtn: 1    // 是否显示关闭按钮 0不显示，1  2 样式
        , anim: 0 //动画类型
        , btn: ['确定', '取消'] //按钮
        , icon: 6    // icon 5是失败
        , yes: session
    });
}

//图片是否向上
var isTop = true;

function show() {
    $('#route').slideToggle("slow");
    if (isTop) {
        isTop = false;
    } else {
        isTop = true;
        $('#myInf').css('height', '');
    }
}

//点击下下一步
function nextBtn(e) {

    if (e === 0) {
        refresh();
    } else {
        $('#myInf').css('display', 'block');
        $('#drawRoute_container').css('display', 'none');
        $('.route_view').remove();
    }
}

//刷新线路
function refresh() {
    if (clickSites.length < 2) {
        layer.msg('至少要有起始站和终点站');
        return;
    }
    $('#myInf').css('display', 'none');
    $('#drawRoute_container').css('display', 'block');
    console.log(clickSites);
    //绘制初始路径
    var path = [];
    $(clickSites).each(function (index, obj) {
        if ($(obj) != null) {
            path.push(obj.position)
        }
    });
    if ($('.sites').length > 0 && isUpdate === 1) {
        console.log("修改进来");
        $.ajax({
            url: "/routeController/updateRoute",
            type: "post",
            data: "routeId=" + $('#routeId')[0].attributes.routeId.nodeValue,
            dataType: "json",
            success: function (data) {
                if (data.length < 1) {
                    layer.msg('服务器旧路线查询失败');
                    return;
                }
                let path = [];
                let size=parseInt(parseInt(data.length)/16)-3;
                console.log(data,'s');
                path.push([data[0].longitude, data[0].latitude]);
                let number=0;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].pid!=-1){
                        console.log("找到一个",data[i])
                        path.push([data[i].longitude, data[i].latitude]);
                        number=0;
                    }else {
                        number++;
                    }
                    if (number===size && path.length<14){
                        path.push([data[i].longitude, data[i].latitude]);
                        number=0;
                    }
                }
                path.push([data[parseInt(data.length) - 1].longitude, data[parseInt(data.length) - 1].latitude]);
                console.log(path, '地址')
                startDraw(path);
            },
            error: function () {
                layer.msg('服务器旧路线查询失败')
            }
        });
    } else {
        startDraw(path);
    }

}


function startDraw(path) {
    if (route) {
        route.destroy();
    }
    map.plugin("AMap.DragRoute", function () {
        route = new AMap.DragRoute(map, path, AMap.DrivingPolicy.LEAST_DISTANCE); //构造拖拽导航类
        route.startMarkerOptions.visible = false;
        route.midMarkerOptions.visible = false;
        route.endMarkerOptions.visible = false;
        route.search(); //查询导航路径并开启拖拽导航
        route.on('addway', function (e) {
            console.log(e)
        })
    });
}


//保存路线
function saveRoute() {
    let index = layer.alert("<div><input style=\"margin-bottom: 20px;\" id='area' placeholder='请输入线路名称' value='" + routeNmae + "' type='text'/> <select id=\"rightOrLeft\"> <option value=\"1\">正</option><option value=\"0\">反</option></select>\n</div>", {
        skin: 'layui-layer-molv' //样式类名  自定义样式
        , title: '保存路线'
        , closeBtn: 1    // 是否显示关闭按钮 0不显示，1  2 样式
        , anim: 0 //动画类型
        , btn: ['确定', '取消'] //按钮
        , icon: 6    // icon 5是失败
        , yes: function () {
            let title = $('#area').val().replace(/\s+/g, "");
            let rightOrLeft = $('#rightOrLeft').val();
            if (title.length < 1) {
                $('#area').attr('placeholder', '请输入有效的线路名称')
            } else {
                console.log(route.getRoute());
                let routeInf = '';
                let startSite = -1;//起始站
                let endSite;
                $(clickSites).each(function (index, obj) {
                    if ($(this) != null) {
                        routeInf += obj.siteId;
                        if (index < clickSites.length - 1) {
                            routeInf += '#'
                        } else {
                            endSite = obj.siteId;
                        }
                        if (startSite === -1) {
                            startSite = obj.siteId;
                        }
                    }
                });
                console.log("选择的方向"+rightOrLeft);
                let addRoute = {
                    routeId:$('#routeId')[0]!==undefined?$('#routeId')[0].attributes.routeId.nodeValue:0,
                    routeInf: routeInf,
                    routeName: title,
                    startSite: startSite,
                    endSite: endSite,
                    rightOrLeft: rightOrLeft,
                    sites: []
                };
                $(route.getRoute()).each(function (i, obj) {
                    let site = {
                        longitude: obj.lng,
                        latitude: obj.lat,
                        isDot: 1,
                        pid: -1
                    };
                    addRoute.sites.push(site);
                });
                console.log(addRoute);

                getSites(addRoute.sites, 0);
                var loading = layer.load(0, {
                    shade: false,
                    time: 5 * 1000
                });
                $.ajax({
                    contentType: "application/json",
                    url: "/routeController/"+(isUpdate===0?"addRoute":"updateRouteById"),
                    type: "post",
                    data: JSON.stringify(addRoute),
                    dataType: "json",
                    success: function (data) {
                        console.log(data);
                        if (data) {
                            layer.close(index);
                            layer.close(loading);
                            layer.msg('保存成功');
                            location.reload();
                        }
                    },
                });
            }
        }
    });
}

//迭代遍历最短距离
function getSites(sites) {
  let size=getNoNull();//获取非空的选择站点有几个
    var i=0;
    $(clickSites).each(function (index, obj) {
        if (obj != null) {
            i++;
            let pid=-1;
            let minDecibels=-1;
            let object=null;
            if (i===1){
                console.log("赋值1")
                sites[0].pid=obj.siteId;
            }else if (i===size){
                sites[sites.length-1].pid=obj.siteId;
                console.log("赋值max")
            }else {
                $(sites).each(function (index2, obj2) {
                    let  distance = getDistance(obj2.latitude, obj2.longitude, obj.position[1], obj.position[0]);
                    if (distance < minDecibels || minDecibels === -1) {
                        pid= obj.siteId;
                        minDecibels = distance;
                        object=obj2;
                    }
                    if (index2===sites.length-1){
                        console.log("查到最短距离", minDecibels);
                        console.log("查到最短距离对象", obj);
                        console.log("查到最短距离对象2", object);
                        object.pid =pid;
                    }
                });
            }

        }
    });
    console.log(sites,"结束")
}

// var number = 0;
// //迭代遍历最短距离
// function getSites(sites,size) {
//     number++;
//     let nub = 0;
//     let nub2 = 0;
//     $(clickSites).each(function (index,obj) {
//         let nubm=0;
//         if (obj != null) {
//             if (obj.minDecibels === -1) {
//                 $(sites).each(function (index,obj2) {
//                     let item = $(this);
//                     let distance = getDistance(obj2.latitude, obj2.longitude, obj.position[1], obj.position[0]);
//                     if (distance<size && obj2.pid===-1 && obj.minDecibels===-1){
//                         console.log("查到最短距离",distance);
//                         console.log("查到最短距离对象",obj);
//                         obj2.pid=obj.siteId;
//                         obj.minDecibels=distance;
//                         return false;
//                     }else{
//                         nubm++;
//                         if (nubm === sites.length) {
//                             console.log("没有",nubm)
//                             nub2++;
//                             if (nub2 === getNoNull()) {
//                                 getSites(sites,number*10);
//                             }
//                         }
//                     }
//                 });
//             } else {
//                 nub++;
//                 if (nub === getNoNull()) {
//                     console.log($(clickSites),"执行完成！");
//                     number=0;
//                 }else {
//                     getSites(sites,number*10);
//                 }
//             }
//         }
//     });
// }

function getNoNull() {
    let nullSize = 0;
    clickSites.forEach(function (obj, index) {
        if (obj != null) {
            nullSize++;
        }
    });
    return nullSize;
}

/**
 * 计算两个经纬度的距离(米)
 */
function getDistance(lat1, lng1, lat2, lng2) {
    var radLat1 = lat1 * Math.PI / 180.0;
    var radLat2 = lat2 * Math.PI / 180.0;
    var a = radLat1 - radLat2;
    var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
        Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    s = s * 6378.137;// EARTH_RADIUS;
    s = Math.round(s * 10000000) / 10000;
    return s;
}





