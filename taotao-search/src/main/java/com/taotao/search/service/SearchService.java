package com.taotao.search.service;


import com.taotao.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String keywords, Integer pageNum, Integer pageSize);
}
