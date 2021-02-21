package com.cykj.service;

import com.cykj.bean.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    int addArticle(Article article);

    List<Article> selectArticles(String page);

    Article selectArticleById(String articleId);
}
