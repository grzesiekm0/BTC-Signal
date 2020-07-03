package com.yourstronghelper.grzegorzmacko.btcsignal;

import com.yourstronghelper.grzegorzmacko.btcsignal.model.Article;

import org.json.JSONObject;

/**
 * This is an example parser to 'parse' pretend json news feed.
 */
public class ArticleParser {
    public static Article parse(JSONObject jsonArticle) {
        Article article = new Article();
        article.setId(jsonArticle.optString("alertId"));
        article.setTitle(jsonArticle.optString("title"));
        article.setContent(jsonArticle.optString("content"));
        article.setLink(jsonArticle.optString("link"));
        return article;
    }
}