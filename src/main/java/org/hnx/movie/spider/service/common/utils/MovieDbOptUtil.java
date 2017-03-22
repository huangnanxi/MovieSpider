package org.hnx.movie.spider.service.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.hnx.movie.spider.dmo.MovieDmo;
import org.hnx.movie.spider.service.common.entity.DbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieDbOptUtil {

    private final static Logger otherLogger = LoggerFactory.getLogger(MovieDbOptUtil.class);

    private static Connection   conn        = null;

    private final static String url         = "jdbc:mysql://localhost:3306/resourceMovie?useUnicode=true&characterEncoding=UTF-8";

    private final static String userName    = "root";

    private final static String password    = "root123";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            otherLogger.error("加载数据库驱动失败", e);

            DbException dbException = new DbException("加载数据库驱动失败", e);
            throw dbException;
        }
    }

    public static void insertMovieItem(MovieDmo movieDmo) {

        String insertSql = constructInsertSql(movieDmo);

        try {
            Statement stmt = conn.createStatement();
            stmt.execute(insertSql);
        } catch (SQLException e) {
            otherLogger.error("执行insertSql失败", e);
            DbException dbException = new DbException("执行insertSql失败", e);
            throw dbException;
        }
    }

    private static String constructInsertSql(MovieDmo movieDmo) {

        String insertSql = "INSERT INTO movie_datas (title,movie_year,country,category,language,movie_desc,poster_img,score,down_url,original_source_url)";

        String valueSql = " VALUES(title,movieYear,country,category,language,movieDesc,posterImg,score,downUrl,originalSourceUrl)";

        Class<MovieDmo> movieDmoClass = MovieDmo.class;
        for (Field field : movieDmoClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Object valueObj = null;
            try {
                Method method = movieDmoClass.getMethod(methodName);
                valueObj = method.invoke(movieDmo);
            } catch (Exception e) {
                otherLogger.error("构造insertSql失败", e);
                DbException dbException = new DbException("构造insertSql失败", e);
                throw dbException;
            }

            String value = null != valueObj ? (String) valueObj : "";

            valueSql = valueSql.replace(fieldName, "'" + value + "'");
        }

        insertSql += valueSql;

        return insertSql;
    }

}
