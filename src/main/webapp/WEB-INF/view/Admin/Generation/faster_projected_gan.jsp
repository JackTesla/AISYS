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
        #submit-btn { height: 30px;}
        .gt-pics { width:512px;height:512px;}
        #picture-div { column-width:520px;}
        .picdel-btn { margin-bottom: 20px;}
        .picDownload { margin-right:20px;}
    </style>
<%--    <p>在下列方框中输入文字描述，然后点击生成图片即可</p>--%>
    <p>预计执行时间5分钟，请耐心等待</p>
    <form name="shellForm" id="shellForm" onsubmit="return false" method="post" action="#">
<%--        <input id="cmdInput" type="text" name="cmd" value="" placeholder="" >--%>
        <button id="submit-btn">生成地震图片</button>
    </form>

    <div>
        <p id="shellOutput"></p>
    </div>
    <div id="picture-div" ></div>
    <c:forEach items="${outputList}" var="p">
        <pre> ${p} </pre><br />
    </c:forEach>

    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript">
        <%--执行结果验证--%>
        $("#submit-btn").click(function () {
            var cmd = $("#cmd").val();
            if (cmd == "" ) {
                alert("请输入命令！")
            } else {
                document.getElementById("shellOutput").innerHTML="<p>正在生成，请稍等...</p>";
                $.ajax({
                    async: false,//同步，待请求完毕后再执行后面的代码
                    type: "POST",
                    url: '/admin/generation/fasterPgSubmit',
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    data: $("#shellForm").serialize(),
                    dataType: "json",
                    success: function (data) {
                        firstGetData(data);
                    },
                    error: function () {
                        alert("数据获取失败")
                    }
                })
            }
        })

        function updatePage(data) {
            var heading = document.getElementById("shellOutput");
            // heading.innerText = "Hello World!";
            var outputs='';
            if(data.code==1) {
                alert(data.msg);
                return;
            }
            for(var str in data.msg) {
                // document.write( data.data[str] );
                outputs += "<pre>"+data.data[str]+"</pre>";
                break;
            }
            heading.innerHTML="out:<br />"+outputs+"<br/>"
            outputs='';
            var picHtml = document.getElementById("picture-div");
            var i=0;
            preloadImages(data.data);
            for(var url in data.data) {
                if(data.data[url] !== undefined) {
                    outputs += "<div>"+"<p>" +
                        "<img class='gt-pics' src=" + data.data[url] + " />" +
                        "<form class='picdelForm' name='picdelForm' method='post' action='#'" + ">" +
                        "<input type=hidden class='del_url' value=" + data.data[url] + " />" +
                        "<a class='picDownload' href="+ data.data[url]+" download>下载</a>" +
                        "<button class='picdel-btn' id='picdel-btn"+i+"' value="+i+" onclick=picDel(this)>删除" + "</button>" +
                        "</form>" +
                        "</p>"+"</div>" ;
                    i++;
                }
            }
            picHtml.innerHTML=outputs;
        }

        //缓存图片
        function preloadImages(array) {
            if (!preloadImages.list) {
                preloadImages.list = [];
            }
            var list = preloadImages.list;
            for (var i = 0; i < array.length; i++) {
                var img = new Image();
                img.onload = function() {
                    var index = list.indexOf(this);
                    if (index !== -1) {
                        // remove image from the array once it's loaded
                        // for memory consumption reasons
                        list.splice(index, 1);
                    }
                }
                list.push(img);
                img.src = array[i];
            }
        }

        // 删除图片按钮
        function picDel(element) {
            var idnum = element.value;
            console.log("delete pic id: "+idnum);
            var delUrl = $(".del_url").eq(idnum).val();
            console.log("del pic url: " + delUrl);
            for( var url in gdata.data) {
                if( gdata.data[url] === delUrl) {
                    delete gdata.data[url];
                    break;
                }
            }
            $.ajax({
                async: false,//同步，待请求完毕后再执行后面的代码
                type: "POST",
                url: '/admin/generation/fasterPgPicDelete',
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                data: {del_url:delUrl},
                dataType: "json",
                success: function (data) {
                    updatePage(gdata);
                },
                error: function () {
                    alert("数据删除失败")
                }
            })
        }

        function firstGetData(data) {
            gdata=data;
            updatePage(data);
        }

    </script>


</rapid:override>

<%@ include file="../Public/framework.jsp" %>
