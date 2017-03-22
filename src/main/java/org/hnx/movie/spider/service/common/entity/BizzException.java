package org.hnx.movie.spider.service.common.entity;

public class BizzException extends RuntimeException {

    private static final long serialVersionUID = -3000814140264412754L;

    public BizzException(String msg) {
        this.msg = msg;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
