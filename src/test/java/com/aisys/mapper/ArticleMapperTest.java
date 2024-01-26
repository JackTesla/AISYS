package com.aisys.mapper;

import com.aisys.BaseTest;
import com.aisys.entity.Article;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Jack
 * @date 2020/10/11 10:53 上午
 */

public class ArticleMapperTest extends BaseTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void countArticleView() {
        int result = articleMapper.countArticleView();
        System.out.println(result);
    }

    @Test
    public void listArticle() {
        List<Article> articleList = articleMapper.listArticle();
        System.out.println(articleList);
    }
}
