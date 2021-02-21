package com.cykj.bean;

import com.cykj.bean.Article;
import com.cykj.util.SpringDataPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ArticleProcessor implements PageProcessor {

    String url = "http://news.163.com/special/0001386F/rank_whole.html";

    @Autowired
    SpringDataPipeline springDataPipeline;

    private Site site = Site.me()
            .setCharset("gbk")//设置编码 utf8 gbk
            .setTimeOut(10 * 1000)//设置 超时时间
            .setRetrySleepTime(3000)//设置重试时间
            .setRetryTimes(3);//设置重试次数

    @Override
    public void process(Page page) {
        if (page!=null){
        List<Selectable> nodes = page.getHtml().css("tr td a").nodes();
        if (nodes.size() == 0) {
            this.saveArticleInfo(page);
        } else {
            for (Selectable selectable : nodes) {
                //将超链接地址加入到任务中
                page.addTargetRequest(selectable.links().toString());
            }
        }
        }
    }

    private void saveArticleInfo(Page page) {
        Html html = page.getHtml();
        List<Selectable> nodes = html.css("h1.post_title").nodes();
        if (nodes.size()<1){
            return;
        }
        Selectable selectable = nodes.get(0);
        //标题
        String title = selectable.xpath("h1/text()").toString();
        if (!isMessyCode(title)) {
            //时间
            String time = html.css("div.post_info", "text").toString().split("来源:")[0];
            //内容
            String content = html.css("div.post_body").nodes().toString();
            content=content.replace("[<div","<div");
            content=content.replace("[</div","</div");
            Article article = new Article();
            article.setTitle(title);
            article.setIssueTime(time);
            article.setContent(content);
            page.putField("article",article);
        } else {
          site.setCharset("utf8");
            page.addTargetRequest(page.getUrl().toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    //定时执行
    //initialDelay当任务起点后等待多常时间执行
    //fixedDelay执行间隔
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void process() {
        Spider.create(new ArticleProcessor())
                .addUrl(url)
                //设置过滤起
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000)))
                .thread(10)
                .addPipeline(springDataPipeline)
                .run();
    }


    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public  boolean isMessyCode(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();

            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
