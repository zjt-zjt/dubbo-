package com.taotao.search.web;

import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import com.taotao.search.service.TbItemSolrManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/solr/manager")
public class SolrManagerController {
    @Autowired
    private TbItemSolrManagerService solrManagerService;
   @Autowired
    private SearchService searchService;
    @CrossOrigin
    @RequestMapping("/import_item")
    @ResponseBody
    public TaotaoResult importTbItem2Solr() {
        TaotaoResult result = solrManagerService.importAllTbItem2Solr();
        return result;
    }

    @RequestMapping("/search")
    @ResponseBody
    public TaotaoResult search(@RequestParam("q") String keywords, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "30") Integer pageSize) {
        SearchResult result = searchService.search(keywords, pageNum, pageSize);
        if(result == null) {
            return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_ERROR, "搜索异常");
        }
        return TaotaoResult.ok(result);
    }

}
