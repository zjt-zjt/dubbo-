package com.taotao.pojo;

import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.List;


public class SearchResult {
    private Long totalPages;
     private   Long pageNum;
     private List<SearchItem> ItemList;


    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public List<SearchItem> getItemList() {
        return ItemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        ItemList = itemList;
    }
}
