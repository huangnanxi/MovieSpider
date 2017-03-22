package org.hnx.movie.spider.service.common.entity;

public class DbException extends RuntimeException {

    private static final long serialVersionUID = 8728554312359955580L;

    public DbException(String message, Exception orgException) {
        this.message = message;
        this.orgException = orgException;
    }

    private String    message;

    private Exception orgException;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getOrgException() {
        return orgException;
    }

    public void setOrgException(Exception orgException) {
        this.orgException = orgException;
    }

}
