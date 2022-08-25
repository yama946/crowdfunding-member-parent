//当前js主要用来在前端生成树形页面

// 生成树形结构的函数
function generateTree() {
    // 1.准备生成树形结构的JSON 数据，数据的来源是发送Ajax 请求得到
    $.ajax({
        "url": "menu/get/whole/tree.json",
        "type":"post",
        "dataType":"json",
        "success":function(response){
            var result = response.result;
            if(result) {
                // 2.创建JSON 对象用于存储对zTree 所做的设置
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom
                    },
                    "data": {
                        "key": {
                            //保证点击菜单节点时的超链接跳转，定义一个不存在的值
                            "url": "maomi"
                        }
                    }
                };
                // 3.从响应体中获取用来生成树形结构的JSON 数据
                var zNodes = response.data;
                // 4.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if(!result) {
                layer.msg(response.data);
            }
        }
    });
}


// 修改默认的图标
function myAddDiyDom(treeId, treeNode) {
    // treeId 是整个树形结构附着的ul 标签的id
    console.log("treeId=" + treeId);

    // 当前树形节点的全部的数据，包括从后端查询得到的Menu 对象的全部属性
    console.log(treeNode);

    // zTree 生成id 的规则
    // 例子：treeDemo_7_ico
    // 解析：ul 标签的id_当前节点的序号_功能
    // 提示：“ul 标签的id_当前节点的序号”部分可以通过访问treeNode 的tId 属性得到
    // 根据id 的生成规则拼接出来span 标签的id
    var spanId = treeNode.tId + "_ico";

    // 根据控制图标的span 标签的id 找到这个span 标签
    // 删除旧的class
    // 添加新的class
    $("#" + spanId)
        .removeClass()
        .addClass(treeNode.icon);
}


// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId,treeNode) {
    // 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>
    // 按钮组出现的位置：节点中treeDemo_n_a 超链接的后面
    // 为了在需要移除按钮组的时候能够精确定位到按钮组所在span，需要给span 设置有规律的id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 判断一下以前是否已经添加了按钮组
    if($("#"+btnGroupId).length > 0) {
        return ;
    }
    // 准备各个按钮的HTML 标签
    var addBtn = "<a id='"+treeNode.id+"' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;'href='javascript:;' title=' 添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg'></i></a>";
    var removeBtn = "<a id='"+treeNode.id+"' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='javascript:;' title=' 删除节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg'></i></a>";
    var updateBtn = "<a id='"+treeNode.id+"' class='updateBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;'  href='javascript:;' title=' 修改节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg'></i></a>";
    // 获取当前节点的级别数据
    var level = treeNode.level;
    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";
    // 判断当前节点的级别
    if(level == 0) {
        // 级别为0 时是根节点，只能添加子节点
        btnHTML = addBtn;
    }
    if(level == 1) {
        // 级别为1 时是分支节点，可以添加子节点、修改
        btnHTML = addBtn + " " + updateBtn;
        // 获取当前节点的子节点数量
        var length = treeNode.children.length;
        // 如果没有子节点，可以删除
        if(length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }
    if(level == 2) {
        // 级别为2 时是叶子节点，可以修改、删除
        btnHTML = updateBtn + " " + removeBtn;
    }
    // 找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";
    // 执行在超链接后面附加span 元素的操作
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>"+btnHTML+"</span>");
}

// 在鼠标离开节点范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode) {
    // 拼接按钮组的id
    var btnGroupId = treeNode.tId + "_btnGrp";
    // 移除对应的元素
    $("#"+btnGroupId).remove();
}