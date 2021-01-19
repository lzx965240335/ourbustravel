var roleId;
var arr = new Array();
var piece_frame;

function btnc(node) {
//角色拥有的菜单
// 显示弹框 及初始化
    roleId = $(node)[0].attributes.value.nodeValue;
    piece_frame = layer.open({
        type: 1,
        title: false,
        area: ['700px', '556px'],
        closeBtn: 0,
        scrollbar: false,
        skin: 'yourclass',
        content: $('.freight-frame-wrap'),
        success: function () {
            $.ajax({
                url: '/MenuController/allMenuByRole',
                type: 'post',
                data: 'roleId=' + roleId,
                dataType: 'json',
                success: function (data) {
                    var sourceArr = data;
                    for (var $province in sourceArr) {
                        // $(".frame_left .f_list_p" + sourceArr[$province].menuId).parents().show();
                        var i = 0;
                        for (var $area in sourceArr[$province]) {
                            $(".frame_left .f_list_a" + sourceArr[$province][$area].menuId).parents(".f_list").parent().hide();
                            for (var j = 0; j < arr.length; j++) {
                                if (sourceArr[$province][$area].pid == arr[j][0]) {
                                    if ($area >= arr[j][1]) {
                                        $(".frame_left .f_list_a" + sourceArr[$province][$area].menuId).parents(".f_list").parent().parent().parent().hide();
                                    }
                                } else {
                                    // console.log(sourceArr[$province][$area].pid);
                                    // console.log(arr[j][0])
                                }
                            }
                            $(".frame_right .f_list_a" + sourceArr[$province][$area].menuId).find(".checkedInput").attr('select', 'selected').parents().show();
                            $(".frame_right .f_list_a" + sourceArr[$province][$area].menuId).parents("ul").siblings(".f_province").addClass("active");
                        }
                    }
                }
            })
        }
    });
}


// 点击确定按钮 返回数据给后台
$(document).on("click", ".freight-frame .confirm", function (e) {
    if (!confirm("点击继续执行（请注意权限分配菜单是否被移除）")) {
        return;
    }
    var sourceArr2 = [];
    var pid = 0;
    $(".frame_right .f_area .checkedInput").each(function () {
        var selected = $(this).attr('select');
        if (selected) {
            var $city = parseInt($(this).parents(".f_area").siblings().find("label").prop('id'));
            // $province = parseInt($(this).parents(".f_city").siblings().find("label").prop('id'));
            // sourceArr2.push($province);
            if (pid != $city) {
                pid = $city;
                sourceArr2.push($city);
            }
            sourceArr2.push(parseInt($(this).parent().prop('id')));
        }
    });
    $.unique(sourceArr2);
    console.log(sourceArr2)
    $.ajax({
        url: '/MenuController/updateRole',
        type: 'post',
        data: 'menuIds=' + sourceArr2 + '&roleId=' + roleId + '&state=' + 1,
        dataType: 'text',
        success: function (data) {
            layer.alert(data);
            if (data = '修改成功') {
                top.location.reload();
            } else {
                layer.close(piece_frame);
            }
        }

    });
});

// 关闭弹框
$(document).on("click", ".freight-frame .cancel, .close-frame-btn", function (e) {
    location.reload();
    layer.close(piece_frame);
});

//菜单所有数据
// 生成弹框数据
$.ajax({
    url: '/MenuController/allMenu',
    type: 'post',
    data: 'powerByRoleMap&roleId=2,3,4,5',
    dataType: 'json',
    success: function (data) {
        var $city = '<ul class="f_province f_city">',
            $data = data;
        //父级菜单
        for (var province in $data) {
            $city += '<li class="f_province_list' + $data[province][0].menuId + '"><span class="f_list"><label id="' + $data[province][0].menuId + '" class="f_list_p' + $data[province][0].menuId + '"><i></i><input class="checkedInput" type="checkbox" /></label><em>' + $data[province][0].menuName + '</em></span><ul class="f_area">';
            for (var area in $data[province]) {
                if (area == 0) {
                    continue;
                }
                $city += '<li><span class="f_list"><label id="' + $data[province][area].menuId + '"  class="f_list_a' + $data[province][area].menuId + '"><i></i><input class="checkedInput" type="checkbox" /></label><em>' + $data[province][area].menuName + '</em></span></li>';
                if ((parseInt(area) + 1) == $data[province].length) {
                    var id = [];
                    id.push($data[province][0].menuId);
                    id.push(area);
                    arr.push(id);
                }
            }
            $city += '</ul></li>';
        }
        $city += '</ul>';
        $("#freight-formwork").append($city);
        $("#freight-formwork_checked").append($city);

        // 全选
        function checkInputAll(selector) {
            $(selector).on("click", function () {
                var checked = $(this).prop("checked");
                if (checked) {
                    $(this).parents(".f_list").siblings("ul").find(".checkedInput").prop("checked", true).siblings("i").addClass("checked");
                } else {
                    $(this).parents(".f_list").siblings("ul").find(".checkedInput").prop("checked", false).siblings("i").removeClass("checked");
                }
            });
        }

        checkInputAll(".frame-wrap .f_province > li > span .checkedInput");
        checkInputAll(".frame-wrap .f_city > li > span .checkedInput");

        // 单选
        function checkInputSolo(selector, parents, parents2) {
            $(selector).on("click", function () {
                var $parent = $(this).parents(parents).children("li"),
                    checkedArr = [], checkedArr2 = [];
                $parent.find(".checkedInput").each(function () {
                    var checked = $(this).prop("checked");
                    checkedArr.push(checked);
                    if (checkedArr.indexOf(false) !== -1) {
                        $(this).parents(parents).siblings(".f_list").find(".checkedInput").prop("checked", false).siblings("i").removeClass("checked");
                    } else {
                        $(this).parents(parents).siblings(".f_list").find(".checkedInput").prop("checked", true).siblings("i").addClass("checked");
                    }
                });
                if (parents2 !== '') {
                    var $parent2 = $(this).parents(parents2).children("li");
                    $parent2.children("span").find(".checkedInput").each(function () {
                        var checked = $(this).prop("checked");
                        checkedArr2.push(checked);
                        if (checkedArr2.indexOf(false) !== -1) {
                            $(this).parents(parents2).siblings(".f_list").find(".checkedInput").prop("checked", false).siblings("i").removeClass("checked");
                        } else {
                            $(this).parents(parents2).siblings(".f_list").find(".checkedInput").prop("checked", true).siblings("i").addClass("checked");
                        }
                    });
                }
            });
        }

        checkInputSolo(".frame-wrap .f_city > li > span .checkedInput", ".frame-wrap .f_city", '');
        checkInputSolo(".frame-wrap .f_area .checkedInput", '.f_area', '.frame-wrap .f_city');
    }
});


// 复选框按钮
$(document).on("click", ".checkedInput", function (e) {
    e.stopPropagation();
    var checked = $(this).prop("checked");
    if (checked) {
        $(this).siblings("i").addClass("checked");
    } else {
        $(this).siblings("i").removeClass("checked");
    }
});

// 展开收缩
$(document).on("click", ".frame-wrap .f_list em", function () {
    $(this).parent().toggleClass("active").siblings("ul").slideToggle();
});

// 右移
$(".frame-wrap .right_btn").on("click", function () {
    $(".frame_left .checkedInput").each(function () {
        var $checked = $(this).prop("checked");
        if ($checked) {
            var $parents = $(this).parents("[class ^= f_province_list]").attr('class'),
                $parent = $(this).parent().attr('class');
            $(".frame_right").find('.' + $parents).find('.' + $parent).find(".checkedInput").prop("checked", true).attr('select', 'selected').siblings("i").addClass("checked").parents(".f_list").addClass("active").parent().show().parents("ul").show().siblings(".f_list").addClass("active").parents().show();
            $(this).parents(".f_list").parent().hide();
        }

    });
});

// 左移
$(".frame-wrap .left_btn").on("click", function () {
    $(".frame_right .checkedInput").each(function () {
        var $checked = $(this).prop("checked");
        if ($checked) {
            var $parents = $(this).parents("[class ^= f_province_list]").attr('class'),
                $parent = $(this).parent().attr('class');
            $(".frame_left").find('.' + $parents).find('.' + $parent).parents(".f_list").addClass("active").parent().show().parents("ul").show().siblings(".f_list").addClass("active").parents().show();
            $(this).removeAttr('select').parents(".f_list").parent().hide();
        }
    });
});




