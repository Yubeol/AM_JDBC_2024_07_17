package org.koreait.dao;

import org.koreait.container.Container;
import org.koreait.dto.Article;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleDao {


    public int doWrite(int memberId, String title, String body) {
        SecSql sql = new SecSql();

        sql.append("INSERT INTO article");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("memberId = ?,", memberId);
        sql.append("title = ?,", title);
        sql.append("`body`= ?;", body);

        return DBUtil.insert(Container.conn, sql);
    }

    public List<Article> getArticles() {
        SecSql sql = new SecSql();

        sql.append("SELECT A.*, M.name AS extra__writer");
        sql.append("FROM article A");
        sql.append("INNER JOIN `member` M");
        sql.append("ON A.memberId = M.id");
        sql.append("ORDER BY id DESC");


        List<Map<String, Object>> articleListMap = DBUtil.selectRows(Container.conn, sql);

        List<Article> articles = new ArrayList<>();

        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }
        return articles;
    }

    public Map<String, Object> getArticleById(int id) {
        SecSql sql = new SecSql();

        sql.append("SELECT A.*, M.name");
        sql.append("FROM article A");
        sql.append("INNER JOIN `member` M");
        sql.append("ON A.memberId = M.id");
        sql.append("WHERE A.id = ?", id);

        return DBUtil.selectRow(Container.conn, sql);
    }

    public void doUpdate(int id, String title, String body) {
        SecSql sql = new SecSql();
        sql.append("UPDATE article");
        sql.append("SET updateDate = NOW()");
        if (title.length() > 0) {
            sql.append(",title = ?", title);
        }
        if (body.length() > 0) {
            sql.append(",`body` = ?", body);
        }
        sql.append("WHERE id = ?", id);

        DBUtil.update(Container.conn, sql);
    }

    public void doDelete(int id) {
        SecSql sql = new SecSql();
        sql.append("DELETE FROM article");
        sql.append("WHERE id = ?", id);

        DBUtil.delete(Container.conn, sql);
    }
}