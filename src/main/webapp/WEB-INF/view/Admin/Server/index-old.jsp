<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="rapid" uri="http://www.rapid-framework.org.cn/rapid" %>
<rapid:override name="title">
    - 服务器列表
</rapid:override>
<rapid:override name="header-style">
    <style>
    </style>
</rapid:override>

<rapid:override name="content">
    <blockquote class="layui-elem-quote">
        <span class="layui-breadcrumb" lay-separator="/">
          <a href="/admin">首页</a>
          <a><cite>服务器主页</cite></a>
        </span>
    </blockquote>
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
        <legend>服务器列表</legend>
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
                <th>name</th>
                <th>ip</th>
                <th>port</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${serverList}" var="p">
                <c:if test="${p.port!=0}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td>${p.ip}</td>
                        <td>${p.port}</td>
                        <td>
                            <a href="/admin/server/edit/${p.id}"
                               class="layui-btn layui-btn-mini">编辑</a>
                            <a href="/admin/server/delete/${p.id}"
                               class="layui-btn layui-btn-danger layui-btn-mini" onclick="return confirmDelete()">删除</a>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </form>
<%--    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">--%>
<%--        <legend>自定义页面</legend>--%>
<%--    </fieldset>--%>
<%--    <div class="layui-form">--%>
<%--        <table class="layui-table" style="width: 40%;">--%>
<%--            <colgroup>--%>
<%--                <col width="150">--%>
<%--                <col width="150">--%>
<%--                <col width="200">--%>
<%--                <col>--%>
<%--            </colgroup>--%>
<%--            <thead>--%>
<%--            <tr>--%>
<%--                <th>别名</th>--%>
<%--                <th>标题</th>--%>
<%--                <th>内容</th>--%>
<%--            </tr>--%>
<%--            </thead>--%>
<%--            <tbody>--%>
<%--                <c:forEach items="${serverList}" var="p">--%>
<%--                    <c:if test="${p.serverStatus==2}">--%>
<%--                        <tr>--%>
<%--                            <td>${p.serverKey}</td>--%>
<%--                            <td>${p.serverTitle}</td>--%>
<%--                            <td><a href="/${p.serverKey}" target="_blank">点击查看</a></td>--%>
<%--                        </tr>--%>
<%--                    </c:if>--%>
<%--                </c:forEach>--%>
<%--            </tbody>--%>
<%--        </table>--%>
<%--    </div>--%>


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
