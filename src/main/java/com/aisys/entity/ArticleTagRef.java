package com.aisys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章和标签关联
 *
 * @author Jack
 * @date 2018/11/17 下午5:20
 */
@Data
public class ArticleTagRef implements Serializable {
    private static final long serialVersionUID = -5816783232020910492L;

    private Integer articleId;

    private Integer tagId;

    public ArticleTagRef() {
    }

    public ArticleTagRef(Integer articleId, Integer tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }
}
