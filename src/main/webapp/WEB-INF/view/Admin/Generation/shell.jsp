<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="rapid" uri="http://www.rapid-framework.org.cn/rapid" %>
<rapid:override name="title">
    - ${title}
</rapid:override>
<rapid:override name="header-style">
    <style>
    </style>
</rapid:override>

<rapid:override name="content">
    <blockquote class="layui-elem-quote">
        <span class="layui-breadcrumb" lay-separator="/">
          <a href="/admin">首页</a>
          <a><cite>${title}</cite></a>
        </span>
    </blockquote>

    <style>
        #cmdInput {
            width: 600px;
            height: 30px;
            padding: 0px;
        }
        #submit-btn {
            height: 30px;
        }
    </style>

<%--    <form name="shellForm" id="shellForm" method="post" action="/admin/server/shell">--%>
    <form name="shellForm" id="shellForm" onsubmit="return false" method="post" action="##">
        <input type="text" id="cmdInput" name="cmd" value="" placeholder="">
        <button id="submit-btn">执行</button>
    </form>

    <c:forEach items="${outputList}" var="p">
        <pre> ${p} </pre><br />
    </c:forEach>

    <div>
        <p id="shellOutput"></p>
    </div>

    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        <%--执行结果验证--%>
        $("#submit-btn").click(function () {
            var cmd = $("#cmd").val();
            if (cmd == "" ) {
                alert("请输入命令！")
            } else {
                $.ajax({
                    async: false,//同步，待请求完毕后再执行后面的代码
                    type: "POST",
                    url: '/admin/server/shellSubmit',
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    data: $("#shellForm").serialize(),
                    dataType: "json",
                    success: function (data) {
                        var heading = document.getElementById("shellOutput");
                        // heading.innerText = "Hello World!";
                        var outputs='';
                        for(var str in data.data) {
                            // document.write( data.data[str] );
                            outputs += "<pre>"+data.data[str]+"</pre>";
                        }
                        heading.innerHTML="out:<br />"+outputs+"<br/>"+heading.innerHTML
                        heading.contentWindow.location.reload(true);
                    },
                    error: function () {
                        alert("数据获取失败")
                    }
                })
            }
        })

    </script>


</rapid:override>

<%@ include file="../Public/framework.jsp" %>
