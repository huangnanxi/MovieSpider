package org.hnx.movie.spider.service.dytt.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hnx.movie.spider.dmo.MovieDmo;
import org.hnx.movie.spider.service.common.entity.BizzException;
import org.hnx.movie.spider.service.common.utils.HttpConnectExecutor;
import org.hnx.movie.spider.service.common.utils.MovieDbOptUtil;
import org.hnx.movie.spider.service.dytt.entity.DyttSpiderRunReqTask;
import org.hnx.movie.spider.service.dytt.utils.DyttBizzLogProUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DyttSpiderAnaylsService {

    private final static Logger otherLogger = LoggerFactory.getLogger(DyttSpiderAnaylsService.class);

    /**
     * 一级解析
     */
    public static void firstSpideAnayLs(DyttSpiderRunReqTask movieSpiderRunReqTask) {
        Document doc = HttpConnectExecutor.connect(movieSpiderRunReqTask.getSourceUrl());

        DyttBizzLogProUtil.docNullDeal(doc, movieSpiderRunReqTask, 1, null);

        try {
            Elements elements = doc.getElementsMatchingOwnText("尾页");
            String html = elements.attr("href");
            int totalNum = Integer.valueOf(html.substring(html.indexOf("index_") + 6, html.indexOf(".html")));
            movieSpiderRunReqTask.setTotalPageNum(totalNum);
            movieSpiderRunReqTask.setCurrPageNum(1);
        } catch (Exception e) {
            DyttBizzLogProUtil.processParseExeception(movieSpiderRunReqTask, 1, e, null);
        }
    }

    /**
     * 二级解析
     */
    public static List<String> secondSpideAnayls(DyttSpiderRunReqTask movieSpiderRunReqTask) {

        List<String> thirdSpideUrlList = new ArrayList<>();

        while (movieSpiderRunReqTask.getCurrPageNum() <= movieSpiderRunReqTask.getTotalPageNum()) {

            // 设置二级解析sourceUrl
            String spideUrl = movieSpiderRunReqTask.constructSecondSpiderUrl();

            Document doc = HttpConnectExecutor.connect(spideUrl);

            try {
                DyttBizzLogProUtil.docNullDeal(doc, movieSpiderRunReqTask, 2, null);
            } catch (BizzException e) {
                continue;
            }

            try {
                Elements elements = doc.getElementsByClass("co_content8");
                Elements innerElements = elements.get(0).getElementsByTag("table");
                boolean hasAdd = false;
                for (int i = 0; i < innerElements.size(); i++) {
                    Elements urlElements = innerElements.get(i).getElementsByTag("a");
                    for (int j = 0; j < urlElements.size(); j++) {
                        if (urlElements.get(j).hasAttr("title")) {
                            String url = "http://www.dy2018.com" + urlElements.get(j).attr("href");
                            thirdSpideUrlList.add(url);
                            hasAdd = true;
                        }
                    }
                }
                // 没有解析出一条三级解析地址，则认作当次二级解析失
                if (!hasAdd) {
                    DyttBizzLogProUtil.processParseExeception(movieSpiderRunReqTask, 2, null, null);
                }
            } catch (Exception e) {
                DyttBizzLogProUtil.processParseExeception(movieSpiderRunReqTask, 2, e, null);
            }

            movieSpiderRunReqTask.setCurrPageNum(movieSpiderRunReqTask.getCurrPageNum() + 1);
        }

        return thirdSpideUrlList;
    }

    /**
     * 三级解析
     */
    public static List<MovieDmo> thirdSpideAnayls(String typeName, List<String> thirdSpideUrlList) {

        List<MovieDmo> movieList = new ArrayList<>();

        for (String url : thirdSpideUrlList) {
            Document doc = HttpConnectExecutor.connect(url);

            try {
                DyttBizzLogProUtil.docNullDeal(doc, null, 3, url);
            } catch (BizzException e) {
                continue;
            }

            try {
                List<Node> nodes = new ArrayList<>();
                for (Node node : doc.getElementById("Zoom").childNodes()) {
                    if (node instanceof Element) {
                        nodes.add(node);
                    }
                }
                String html = doc.getElementById("Zoom").html().replaceAll("&nbsp;", "").replaceAll("\n", "")
                        .replaceAll("[　*| *| *|//s*]*", "");

                MovieDmo movie = new MovieDmo();
                movie.setTitle(doc.getElementsByClass("title_all").get(0).text());
                movie.setCategory(strSpecialSub1(html, "◎类别"));
                if (null == movie.getCategory()) {
                    movie.setCategory(typeName);
                }
                movie.setDownUrl(
                        doc.getElementById("Zoom").getElementsByTag("table").get(0).getElementsByTag("a").text());
                movie.setOriginalSourceUrl(url);

                try {
                    movie.setScore(doc.getElementsByClass("rank").get(0).text());
                } catch (Exception e) {
                    otherLogger.error("dytt:第三级解析非关键字段发生异常", e);
                }

                try {
                    movie.setPosterImg(((Element) nodes.get(0)).getElementsByTag("img").get(0).attr("src"));
                } catch (Exception e) {
                    otherLogger.error("dytt:第三级解析非关键字段发生异常", e);
                }

                movie.setMovieYear(strSpecialSub1(html, "◎年代"));
                movie.setCountry(strSpecialSub1(html, "◎国家"));
                movie.setLanguage(strSpecialSub1(html, "◎语言"));
                movie.setMovieDesc(strSpecialSubForDesc(html, "◎简介"));

                movieList.add(movie);
            } catch (Exception e) {
                otherLogger.error("dytt:第三级解析关键字段发生异常", e);
                DyttBizzLogProUtil.processParseExeception(null, 3, e, url);
            }

        }

        return movieList;
    }

    public static void landMovieItems(List<MovieDmo> movieDmos) {
        for (MovieDmo movieDmo : movieDmos) {
            try {
                MovieDbOptUtil.insertMovieItem(movieDmo);

                DyttBizzLogProUtil.logMovieItemLand(movieDmo, true);
            } catch (Exception e) {
                DyttBizzLogProUtil.logMovieItemLand(movieDmo, false);
            }
        }
    }

    private static String strSpecialSub1(String str, String keyword) {
        String resultStr = null;
        try {
            if (str.indexOf(keyword) != -1) {
                String tmpStr = str.substring(str.indexOf(keyword) + keyword.length());
                resultStr = tmpStr.substring(0, tmpStr.indexOf("<"));
            }
        } catch (Exception e) {
            otherLogger.error("dytt:第三级解析非关键字段发生异常", e);
        }

        return resultStr;
    }

    private static String strSpecialSubForDesc(String str, String keyword) {
        String resultStr = null;
        try {

            if (str.indexOf(keyword) != -1) {
                String tmpStr = str.substring(str.indexOf(keyword) + keyword.length());

                Pattern pattern1 = Pattern.compile("(<[a-z]>){2,}");
                Matcher matcher = pattern1.matcher(tmpStr);

                if (matcher.find()) {
                    if (tmpStr.startsWith(matcher.group(0))) {
                        tmpStr = tmpStr.substring(tmpStr.indexOf(matcher.group(0)) + matcher.group(0).length());
                        resultStr = tmpStr.substring(0, tmpStr.indexOf("<"));
                    }
                }
            }
        } catch (Exception e) {
            otherLogger.error("dytt:第三级解析非关键字段发生异常", e);
        }

        return resultStr;
    }

}
