package br.com.next;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataModel {
    private Long id;
    private String vendor;
    private Long commentCount;
    private Date date;
    private Boolean dev;
    private Long commits;
    private Long files;

    public DataModel(Long id, String vendor, Long commentCount, Date date, Boolean dev, Long commits, Long files) {
        this.id = id;
        this.vendor = vendor;
        this.commentCount = commentCount;
        this.date = date;
        this.dev = dev;
        this.files = files;
        this.commits = commits;
    }

    public String getVendor() {
        return vendor;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public Long getId() {
        return id;
    }

    public boolean isDev() {
        return this.dev;
    }

    public Long getCommits() {
        return commits;
    }

    public Long getFiles() {
        return files;
    }

    @Override
    public String toString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return "DataModel{" +
                "id=" + id +
                ", vendor='" + vendor + '\'' +
                ", commentCount=" + commentCount +
                ", date=" + simpleDateFormat.format(date) +
                ", dev=" + dev +
                ", commits=" + commits +
                ", files=" + files +
                '}';
    }
}
