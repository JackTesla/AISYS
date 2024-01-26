package com.aisys.controller.home;

import com.aisys.enums.UserRole;
import com.github.pagehelper.PageInfo;
import com.aisys.entity.Link;

import com.aisys.enums.ArticleStatus;
import com.aisys.enums.LinkStatus;
import com.aisys.enums.NoticeStatus;

import com.aisys.entity.*;
import com.aisys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * 用户的controller
 *
 * @author Jack
 * @date 2017/8/24
 */
@Controller
public class IndexController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

//    @RequestMapping(value = {"/", "/article"})
//    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
//                        @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {
//        HashMap<String, Object> criteria = new HashMap<>(1);
//        criteria.put("status", ArticleStatus.PUBLISH.getValue());
//        //文章列表
//        PageInfo<Article> articleList = articleService.pageArticle(pageIndex, pageSize, criteria);
//        model.addAttribute("pageInfo", articleList);
//
//        //公告
//        List<Notice> noticeList = noticeService.listNotice(NoticeStatus.NORMAL.getValue());
//        model.addAttribute("noticeList", noticeList);
//        //友情链接
//        List<Link> linkList = linkService.listLink(LinkStatus.NORMAL.getValue());
//        model.addAttribute("linkList", linkList);
//
//        //侧边栏显示
//        //标签列表显示
//        List<Tag> allTagList = tagService.listTag();
//        model.addAttribute("allTagList", allTagList);
//        //最新评论
//        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);
//        model.addAttribute("recentCommentList", recentCommentList);
//        model.addAttribute("pageUrlPrefix", "/article?pageIndex");
//        return "Home/index";
//    }

    @RequestMapping(value = {"/article", })
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user==null) return "Admin/login";
        Integer userId = null;
        if (!UserRole.ADMIN.getValue().equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            userId = user.getUserId();
        }
        //文章列表
        List<Article> articleList = articleService.listRecentArticle(userId, 5);
        model.addAttribute("articleList", articleList);

        //评论列表
        List<Comment> commentList = commentService.listRecentComment(userId, 5);
        model.addAttribute("commentList", commentList);
        return "Admin/index";
    }

    @RequestMapping(value = "/search")
    public String search(
            @RequestParam("keywords") String keywords,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {
        //文章列表
        HashMap<String, Object> criteria = new HashMap<>(2);
        criteria.put("status", ArticleStatus.PUBLISH.getValue());
        criteria.put("keywords", keywords);
        PageInfo<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articlePageInfo);

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);
        //获得随机文章
        List<Article> randomArticleList = articleService.listRandomArticle(8);
        model.addAttribute("randomArticleList", randomArticleList);
        //获得热评文章
        List<Article> mostCommentArticleList = articleService.listArticleByCommentCount(8);
        model.addAttribute("mostCommentArticleList", mostCommentArticleList);
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);
        model.addAttribute("recentCommentList", recentCommentList);
        model.addAttribute("pageUrlPrefix", "/search?keywords=" + keywords + "&pageIndex");
        return "Home/Page/search";
    }

    @RequestMapping("/404")
    public String NotFound(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/404";
    }


    @RequestMapping("/403")
    public String Page403(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/403";
    }

    @RequestMapping("/500")
    public String ServerError(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/500";
    }


}




