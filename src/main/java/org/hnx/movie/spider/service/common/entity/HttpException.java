package org.hnx.movie.spider.service.common.entity;

public class HttpException extends RuntimeException {

    private static final long serialVersionUID = 5208434319095866122L;

    public HttpException(String url, String message, Exception orgException) {
        this.url = url;
        this.message = message;
        this.orgException = orgException;
    }

    private String    url;

    private String    message;

    private Exception orgException;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        String str = "url: " + url + " occur access error. ErrorMsg is: " + message;
        return str;
    }

    public Exception s() {
        return orgException;
    }

    public void setOrgException(Exception orgException) {
        this.orgException = orgException;
    }

}
