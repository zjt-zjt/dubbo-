package com.taotao.portal.service.impl;

import com.taotao.common.HttpUtil;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class SearchServiceImpl implements SearchService {
//    @Value("http://152.136.119.210:8983/solr/taotao")
//    private String searchUrl;

    @Override
    public SearchResult search(String keywords, Integer pageNum, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("q", keywords);
        params.put("pageNum", pageNum+"");
        params.put("pageSize", pageSize+"");
        String searchResultJson = HttpUtil.doPost("http://localhost:8083/search/solr/manager/search", params,"UTF-8");
        TaotaoResult taotaoResult = TaotaoResult.formatToPojo(searchResultJson, SearchResult.class);
        if(taotaoResult.getStatus().equals(SystemConstants.TAOTAO_RESULT_STATUS_OK)) {
            return (SearchResult) taotaoResult.getData();
        }
        return null;
    }
}
