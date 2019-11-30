package com.taotao.portal.service;

import com.taotao.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String keywords, Integer pageNum, Integer pageSize);
}
