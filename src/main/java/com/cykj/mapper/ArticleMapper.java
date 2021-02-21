package com.cykj.mapper;

import com.cykj.bean.Article;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ArticleMapper {


    int addArticle(Article article);

    List<Article> selectArticles(Map map);

    Article selectArticleById(int articleId);
}
