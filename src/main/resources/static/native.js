/**
 option对象有如下属性:

 1)请求方式:method

 2)请求的url:url

 3)是否异步:asyn

 4)请求体:params

 5)回调方法:callback

 6)服务器响应数据转换成什么类型:type
 */
function ajax(option) {

    // 得到XMLHttpRequest(大多数浏览器)
    var xmlHttpRequest = new XMLHttpRequest();

    // 设置请求方式(默认为GET请求)
    if (!option.method) {
        option.method = "GET";
    }

    // 默认为异步处理
    if (!option.asyn) {
        option.asyn = true;
    }

    // 打开连接
    xmlHttpRequest.open(option.method, option.url, option.asyn);

    // 判断是否为POST
    if ("POST" === option.method) {
        xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    // 发送请求
    xmlHttpRequest.send(option.params);

    // 注册监听
    xmlHttpRequest.onreadystatechange = function () {

        if (xmlHttpRequest.readyState === 4 && xmlHttpRequest.status === 200) {

            var data = '';

            // 如果type没有赋值, 那么默认为文本
            if (!option.type) {
                data = xmlHttpRequest.responseText;
            } else if (option.type === "xml") {
                data = xmlHttpRequest.responseXML;
            } else if (option.type === "text") {
                data = xmlHttpRequest.responseText;
            } else if (option.type === "json") {
                var text = xmlHttpRequest.responseText;
                data = eval("(" + text + ")");
            }

            // 调用回调方法
            option.callback(data);
        }
    };
}

function addProvinceOption(province) {

    // 创建option标签节点对象
    var option = document.createElement("option");
    // 创建文本节点
    var textNode = document.createTextNode(province['name']);

    option.appendChild(textNode);
    // 为option的属性value赋值
    option.setAttribute("value", province['pid']);

    document.getElementById("province").appendChild(option);
}

function removeOptionChild(select) {

    var options = select.getElementsByTagName("option");

    while (options.length > 1) {
        // 除了第一个option元素外全部删除
        select.removeChild(options[1]);
    }
}

function addCityOption(city) {

    // 创建option标签节点对象
    var option = document.createElement("option");
    // 创建文本节点
    var textNode = document.createTextNode(city['name']);

    option.appendChild(textNode);
    // 为option的属性value赋值
    option.setAttribute("value", city['cid']);

    document.getElementById("city").appendChild(option);
}

function ajaxJq(url, data, method, async, call) {

    $.ajax({
        url: url,
        data: data,
        type: method,
        async: async,
        cache: false,
        dataType: 'text',
        xhrFields: {
            withCredentials: true
        },
        success: function (result) {
            call(result, url, data, method);
        },
        error: function () {
            alert(url + "," + "网络异常");
        }
    });
}

/**
 * 获取url传递过来的参数
 * @param paramName 参数名称
 * @returns string 参数值
 */
function getParam(paramName) {
    //debugger;
    var href = window.location.href;

    var index = href.indexOf("?");
    if (index === -1) {
        return "";
    }
    var paramStr = href.substr(index + 1);
    if (!paramStr) {
        return "";
    }

    var params = paramStr.split("&");

    for (var i = 0; i < params.length; i++) {
        var paramArr = params[i].split("=");
        if (paramArr.length === 2 && paramArr[0] === paramName) {
            return paramArr[1];
        }
    }

    return "";
}

