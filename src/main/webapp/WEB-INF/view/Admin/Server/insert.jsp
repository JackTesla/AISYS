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
        .layui-form-item .layui-input-inline {
            width: 300px;
        }

        .layui-word-aux {
            color: #FF5722 !important;
        }
    </style>
</rapid:override>

<rapid:override name="content">

    <blockquote class="layui-elem-quote">
         <span class="layui-breadcrumb" lay-separator="/">
              <a href="/admin">首页</a>
              <a href="/admin/server">服务器管理</a>
              <a><cite>${title}</cite></a>
        </span>
    </blockquote>
    <br><br>
    <form class="layui-form" action="/admin/server/insertSubmit" id="serverForm"
          method="post">
        <div class="layui-form-item">
            <label class="layui-form-label">服务器名称 <span style="color: #FF5722; ">*</span></label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="serverName" required
                       lay-verify="serverName"
                       autocomplete="off" class="layui-input" onblur="checkServerName()">
            </div>
            <div class="layui-form-mid layui-word-aux" id="serverNameTips"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">服务器ip <span style="color: #FF5722; ">*</span></label>
            <div class="layui-input-inline">
                <input type="text" name="ip" id="userName" required
                       lay-verify="serverName"
                       autocomplete="off" class="layui-input" onblur="checkServerIp()">
            </div>
            <div class="layui-form-mid layui-word-aux" id="ipTips"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码 <span style="color: #FF5722; ">*</span></label>
            <div class="layui-input-inline">
                <input type="password" name="password"  id="userPass" required
                       lay-verify="serverPass"
                       autocomplete="off" class="layui-input" min="3" max="20">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">ssh端口 <span style="color: #FF5722; ">*</span></label>
            <div class="layui-input-inline">
                <input type="text" name="port"  required
                       placeholder="" autocomplete="off" min="2" max="10"
                       class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="demo2" id="submit-btn">保存</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>

</rapid:override>
<rapid:override name="footer-script">

</rapid:override>

<%@ include file="../Public/framework.jsp" %>
