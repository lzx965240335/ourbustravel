package com.cykj.util;

import com.cykj.bean.Article;
import com.cykj.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {

    @Autowired
    ArticleService articleService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的文章对象
        Article article = resultItems.get("article");
        //判断数据是否不为空
        if (article!=null){
            articleService.addArticle(article);
        }

    }
}
