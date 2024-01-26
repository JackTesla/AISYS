<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="rapid" uri="http://www.rapid-framework.org.cn/rapid" %>
<rapid:override name="title">
    - 页面列表
</rapid:override>
<rapid:override name="header-style">
    <style>
    </style>
</rapid:override>

<rapid:override name="content">
    <blockquote class="layui-elem-quote">
        <span class="layui-breadcrumb" lay-separator="/">
          <a href="/admin">首页</a>
          <a><cite>执行SHEEL</cite></a>
        </span>
    </blockquote>

<%--    <form name="shellForm" id="shellForm" method="post" action="/admin/server/shell">--%>
    <form name="shellForm" id="shellForm" onsubmit="return false" method="post" action="##">
        <input type="text" name="cmd" value="" placeholder="">
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



    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>页面列表</legend>
    </fieldset>
    <form id="pageForm" method="post">
        <table class="layui-table">
            <colgroup>
                <col width="50">
                <col width="50">
                <col width="100">
                <col width="200">
                <col width="50">
                <col width="100">
            </colgroup>
            <thead>
            <tr>
                <th>id</th>
                <th>key</th>
                <th>标题</th>
                <th>内容</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${pageList}" var="p">
                <c:if test="${p.pageStatus!=2}">
                    <tr>
                        <td>${p.pageId}</td>
                        <td>${p.pageKey}</td>
                        <td>
                            ${p.pageTitle}
                        </td>
                        <td>
                            <a href="/${p.pageKey}"
                               target="_blank">
                                    ${fn:substring(p.pageContent, 0,20 )}

                            </a>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.pageStatus==1}">
                                    显示
                                </c:when>
                                <c:otherwise>
                                    <span style="color: #FF5722;">
                                        隐藏
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="/admin/page/edit/${p.pageId}"
                               class="layui-btn layui-btn-mini">编辑</a>
                            <a href="/admin/page/delete/${p.pageId}"
                               class="layui-btn layui-btn-danger layui-btn-mini" onclick="return confirmDelete()">删除</a>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </form>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>自定义页面</legend>
    </fieldset>
    <div class="layui-form">
        <table class="layui-table" style="width: 40%;">
            <colgroup>
                <col width="150">
                <col width="150">
                <col width="200">
                <col>
            </colgroup>
            <thead>
            <tr>
                <th>别名</th>
                <th>标题</th>
                <th>内容</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${pageList}" var="p">
                    <c:if test="${p.pageStatus==2}">
                        <tr>
                            <td>${p.pageKey}</td>
                            <td>${p.pageTitle}</td>
                            <td><a href="/${p.pageKey}" target="_blank">点击查看</a></td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>
    </div>


    <blockquote class="layui-elem-quote layui-quote-nm">
        温馨提示： <br>
        1、自定义的页面，无法删除，别名已写入控制器
    </blockquote>
</rapid:override>
<rapid:override name="footer-script">
    <script>


    </script>
</rapid:override>
<%@ include file="../Public/framework.jsp" %>
