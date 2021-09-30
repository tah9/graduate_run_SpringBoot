package com.tah.graduate_run.entity;


import java.util.List;

public class Page<T> {
   private List<T> rows;
   private Integer total;
   private Integer pagerNum;
   private Integer pagerSize;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPagerNum() {
        return pagerNum;
    }

    public void setPagerNum(Integer pagerNum) {
        this.pagerNum = pagerNum;
    }

    public Integer getPagerSize() {
        return pagerSize;
    }

    public void setPagerSize(Integer pagerSize) {
        this.pagerSize = pagerSize;
    }
}
