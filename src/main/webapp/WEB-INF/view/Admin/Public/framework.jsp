<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="rapid" uri="http://www.rapid-framework.org.cn/rapid" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <link rel="shortcut icon" href="/img/logo.png">
    <title>
            AI图像生成后台
            <rapid:block name="title"></rapid:block>
    </title>
    <link rel="stylesheet" href="/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="/css/back.css">
    <link rel="stylesheet" href="/plugin/font-awesome/css/font-awesome.min.css">
    <rapid:block name="header-style"></rapid:block>
    <rapid:block name="header-script"></rapid:block>
</head>
<body>
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo"><a href="/admin" style="color:#009688;">
        ${options.optionSiteTitle}
        </a>
        </div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"><a href="/" target="_blank">前台</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;">新建</a>
                <dl class="layui-nav-child">
                    <dd><a href="/admin/page/insert">页面</a></dd>
                    <dd><a href="/admin/category/insert">分类</a></dd>
                    <dd><a href="/admin/notice/insert">公告</a></dd>
                    <dd><a href="/admin/link/insert">链接</a></dd>
                </dl>
            </li>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="${sessionScope.user.userAvatar}" class="layui-nav-img">
                    ${sessionScope.user.userName}
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="/admin/profile">基本资料</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item">
                <a href="/admin/logout">退出</a>
            </li>
        </ul>
    </div>

    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->

            <c:if test="${sessionScope.user != null && sessionScope.user.userRole == 'admin'}">
                <ul class="layui-nav layui-nav-tree"  lay-filter="test">
                    <li class="layui-nav-item layui-nav-itemed">
                        <a href="javascript:;">服务器管理</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/server/list">服务器列表</a></dd>
                            <dd><a href="/admin/server/insert">添加服务器</a></dd>
                            <dd><a href="/admin/server/shell">执行shell</a></dd>
                            <dd><a href="/admin/server/gpuinfo">查看显卡</a></dd>
                            <dd><a href="/admin/server/memory">内存状态</a></dd>
                            <dd><a href="/admin/server/harddisk">查看硬盘</a></dd>
                            <dd><a href="/admin/server/xml2pdf">xml转pdf</a></dd></dl>
                    </li>
                    <li class="layui-nav-item">
                        <a href="javascript:;">图像生成</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/generation/sd">stable diffusion</a></dd>
                            <dd><a href="/admin/generation/sd2">stable diffusion2</a></dd>
                            <dd><a href="/admin/generation/pg">projected-gan</a></dd>
                            <dd><a href="/admin/generation/pg-dsc">projected-gan-dsc</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item">
                        <a href="javascript:;">页面</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/page">全部页面</a></dd>
                            <dd><a href="/admin/page/insert">添加页面</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item">
                        <a href="javascript:;">用户</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/user">全部用户</a></dd>
                            <dd><a href="/admin/user/insert">添加用户</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item">
                        <a href="javascript:;">设置</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/menu">菜单</a></dd>
                            <dd><a href="/admin/options">主要选项</a></dd>
                        </dl>
                    </li>
                </ul>
            </c:if>

            <c:if test="${sessionScope.user != null && sessionScope.user.userRole == 'user'}">
                <ul class="layui-nav layui-nav-tree"  lay-filter="test" >
                    <li class="layui-nav-item layui-nav-itemed">
                        <a href="javascript:;">图像生成</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/generation/sd">stable diffusion</a></dd>
                            <dd><a href="/admin/generation/sd2">stable diffusion2</a></dd>
                            <dd><a href="/admin/generation/pg">projected-gan</a></dd>
                            <dd><a href="/admin/generation/fasterPg">faster-projected-gan</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item ">
                        <a href="javascript:;">服务器管理</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/admin/server/list">服务器列表</a></dd>
                            <dd><a href="/admin/server/insert">添加服务器</a></dd>
                            <dd><a href="/admin/server/shell">执行shell</a></dd>
                            <dd><a href="/admin/server/gpuinfo">查看显卡</a></dd>
                            <dd><a href="/admin/server/memory">内存状态</a></dd>
                            <dd><a href="/admin/server/harddisk">查看硬盘</a></dd>
                            <dd><a href="/admin/server/xml2pdf">xml转pdf</a></dd></dl>
                    </li>
                </ul>
            </c:if>
        </div>
    </div>

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">
            <rapid:block name="content">

            </rapid:block>
        </div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © <a href="https://www.github.com/JackTesla">项目github</a>
    </div>
</div>

<script src="/js/jquery.min.js"></script>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/js/back.js"></script>
<rapid:block name="footer-script">

</rapid:block>
<script>
    //给文本编辑器的iframe引入代码高亮的css
    $("iframe").contents().find("head").append("<link rel=\"stylesheet\" href=\"/css/highlight.css\">\n");

</script>

</body>
</html>
