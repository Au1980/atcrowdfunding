<%--
  Created by IntelliJ IDEA.
  User: xyz25
  Date: 2020/7/29
  Time: 22:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function () {
                $.ajax({
                    "url":"send/array/one.html",
                    "type":"post",
                    "data":{"array":[5,8,12]},
                    "dataType":"text", // 服务器返回数据解析格式
                    "success":function (response) {
                        alert(response)
                    },
                    "error":function (response) {
                        alert(response)
                    },
                })
            })
        })

        $(function () {
            $("#btn2").click(function () {
                $.ajax({
                    "url":"send/array/two.html",
                    "type":"post",
                    "data":{
                        "array[0]":5,
                        "array[1]":8,
                        "array[2]":12
                    },
                    "dataType":"text", // 服务器返回数据解析格式
                    "success":function (response) {
                        alert(response)
                    },
                    "error":function (response) {
                        alert(response)
                    }
                })
            })
        })

        $(function () {
            $("#btn3").click(function () {
                // 准备好要发送到服务器的数组
                var array = [5, 8, 12];

                // 将json数组转换为json字符串
                var requestBody = JSON.stringify(array);
                console.log(requestBody)

                $.ajax({
                    "url":"send/array/three.html",
                    "type":"post",
                    "data": requestBody,
                    "contentType":"application/json;charset=UTF-8",
                    "dataType":"text", // 服务器返回数据解析格式
                    "success":function (response) {
                        alert(response)
                    },
                    "error":function (response) {
                        alert(response)
                    }
                })
            })
        })

        $(function() {
            $("#btn5").click(function () {
                layer.msg("Layer的弹框");
            });
        })

    </script>
</head>
<body>
    <a href="test/ssm.html">测试ssm环境</a>
    <br/>
    <a href="admin/to/login/page.html">后台登录页面</a>
    <br/>
    <button id="btn1">Send [5, 8, 12] One</button>
    <br/>
    <button id="btn2">Send [5, 8, 12] Two</button>
    <br/>
    <button id="btn3">Send [5, 8, 12] Three</button>
    <br/>
    <button id="btn5">点我弹框</button>
</body>
</html>
