package org.hnx.movie.spider.service.dytt.entity;

public class DyttSpiderRunReqTask {

    public DyttSpiderRunReqTask(String typeName, String sourceUrl) {
        this.typeName = typeName;
        this.sourceUrl = sourceUrl;
    }

    private String typeName;

    private String sourceUrl;

    // 内部设值内容，传参无需设值
    private int    totalPageNum;

    private int    currPageNum;

    public String constructSecondSpiderUrl() {
        String spideUrl = sourceUrl;
        if (1 != currPageNum) {
            spideUrl = spideUrl + "index_" + currPageNum + ".html";
        }
        return spideUrl;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getCurrPageNum() {
        return currPageNum;
    }

    public void setCurrPageNum(int currPageNum) {
        this.currPageNum = currPageNum;
    }

}
