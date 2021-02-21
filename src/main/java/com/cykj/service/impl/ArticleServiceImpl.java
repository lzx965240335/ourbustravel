package com.cykj.service.impl;

import com.cykj.bean.Article;
import com.cykj.mapper.ArticleMapper;
import com.cykj.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public int addArticle(Article article) {
        return articleMapper.addArticle(article);
    }

    //查找文章
    @Override
    public List<Article> selectArticles(String page) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("start",(Integer.parseInt(page)-1)*5);
        map.put("end",5);
        return articleMapper.selectArticles(map);
    }

    //ID查找文章
    @Override
    public Article selectArticleById(String articleId) {
        return articleMapper.selectArticleById(Integer.parseInt(articleId));
    }

}
