package com.taotao.search.service.impl;

import com.taotao.pojo.SearchItem;
import com.taotao.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public SearchResult search(String keywords, Integer pageNum, Integer pageSize) {

        SolrQuery query = new SolrQuery();
        // 设置查询关键字
        query.setQuery(keywords);

        // 设置默认查询字段
        query.set("df", "item_keywords");

        // 设置搜索分页参数
        query.setStart((pageNum - 1) * pageSize);
        query.setRows(pageSize);

        // 设置关键词高亮显示
        String hlField = "item_title";
        query.setHighlight(true);  // 开启Solr关键词高亮
        query.addHighlightField(hlField);  // 添加支持高亮的字段
        query.setHighlightSimplePre("<em style='color:red;'>");    //设置高亮的关键词前面附加的高亮信息
        query.setHighlightSimplePost("</em>"); //设置高亮的关键词后面附加的高亮信息
        try {
            // 获取搜索结果
            QueryResponse resp = null;
            try {
                resp = solrClient.query(query);
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SolrDocumentList resultList = resp.getResults();

            /**
             *
             * Solr返回的高亮信息格式：
             * {
             *    "1202453093":{
             *        "item_title":[
             *            "唐为 TW509 移动联通2G<em>手机</em> 翻盖<em>手机</em> 双卡双待 老人<em>手机</em> 金色"
             *        ]
             *     }
             * }
             *
             *
             * 获取Solr关键词高亮
             * 外层Map： key -> id  value: Map
             * 内层Map: key -> 咱们设置的高亮字段名 value: List
             * 最内层的List: 每个元素是一个已经携带了高亮信息的高亮字段内容（）
             */
            Map<String, Map<String, List<String>>> highlighting = resp.getHighlighting();

            SearchResult searchResult = new SearchResult();

            // 获取搜索结果
            List<SearchItem> searchItemList = parseSearchDocument(resultList, highlighting, hlField);
            searchResult.setItemList(searchItemList);

            // 获取搜索到的总记录数
            long totalCount = resultList.getNumFound();

            // 计算总页数
            long totalPages = totalCount / pageSize;
            if(totalCount % pageSize != 0) {
                totalPages ++;
            }
            searchResult.setPageNum(pageNum.longValue());
            searchResult.setTotalPages(totalPages);
            return searchResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Solr结果封装成我们自己的SearchItem对象
     * @param resultList
     * @return
     */
    private List<SearchItem> parseSearchDocument(SolrDocumentList resultList) {
        return parseSearchDocument(resultList, null, "item_title");
    }

    /**
     * 将Solr结果封装成我们自己的SearchItem对象
     * @param resultList   搜索结果
     * @param highlighting 高亮信息
     * @return
     */
    private List<SearchItem> parseSearchDocument(SolrDocumentList resultList, Map<String, Map<String, List<String>>> highlighting, String hlField) {
        List<SearchItem> searchItemList = new ArrayList<>();
        for(Iterator<SolrDocument> it = resultList.iterator(); it.hasNext();) {
            SolrDocument doc = it.next();
            String title = (String) doc.getFieldValue("item_title");
            String image = (String) doc.getFieldValue("item_image");
            String categoryName = (String) doc.getFieldValue("item_category_name");
            String itemDesc = (String) doc.getFieldValue("item_desc");
            String itemSellPoint = (String) doc.getFieldValue("item_sell_point");

            String sid = (String) doc.getFieldValue("id");
            Long id = Long.parseLong(sid);
            Long price = (Long) doc.getFieldValue("item_price");

            /**
             * 处理Solr商品标题高亮
             */
            if(highlighting != null && highlighting.size() > 0) {
                if(highlighting.containsKey(sid)) {
                    Map<String, List<String>> highlightMap = highlighting.get(sid);
                    if(highlightMap != null && highlightMap.size() > 0) {
                        String hlItemTitle = highlightMap.get(hlField).get(0);
                        title = hlItemTitle;
                    }
                }
            }

            SearchItem searchItem = new SearchItem();
            searchItem.setId(id);
            if(!(image==null)){
                searchItem.setImage(image.split(",")[0]);
            }
            searchItem.setTitle(title);
            searchItem.setPrice(price);
            searchItem.setCategoryName(categoryName);
            if(!(itemDesc==null)){
                searchItem.setItemDesc(itemDesc);
            }
            searchItem.setSellPoint(itemSellPoint);
            searchItemList.add(searchItem);
        }
        return searchItemList;
    }

}
