package com.taotao.search.service.impl;

import com.taotao.common.ExceptionUtil;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.SearchItem;
import com.taotao.search.service.TbItemSolrManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemSolrManagerServiceImpl implements TbItemSolrManagerService {

    @Value("${spring.data.solr.batch_size}")
    private Integer batchSize;

    @Autowired(required = false)
    private TbItemMapper tbItemMapper;

    @Autowired
    private SolrClient solrClient;
    @Override
    public TaotaoResult importAllTbItem2Solr() {
        // 从数据库中查询数据
        List<SearchItem> itemList = tbItemMapper.selectAllSearchItem();

        try {
            // 定义批量操作暂存队列
            List<SolrInputDocument> batchList = new ArrayList<>();
            for(int i = 0; i< itemList.size(); i++) {
                //将对象转换为SolrInputDocument对象
                SearchItem item = itemList.get(i);
                SolrInputDocument doc = new SolrInputDocument();
                doc.addField("id", item.getId());
                doc.addField("item_title", item.getTitle());
                doc.addField("item_category_name", item.getCategoryName());
                doc.addField("item_desc", item.getItemDesc());
                doc.addField("item_image", item.getImage());
                doc.addField("item_price", item.getPrice());
                doc.addField("item_sell_point", item.getSellPoint());

                // 将创建好的待存的SolrInputDocument对象添加到批量暂存队列
                batchList.add(doc);
                // 批量添加到solrClient
                if(batchList.size() % batchSize == 0) {
                    System.out.println("批量导入solr"+batchSize+"条");
                    solrClient.add(batchList);
                    batchList.clear();
                }
            }
            // 提交剩下的数据
            if(batchList.size() > 0) {
                System.out.println("批量导入solr"+batchList.size()+"条");
                solrClient.add(batchList);
                batchList.clear();
            }
            // 提交到Solr服务器
            solrClient.commit();
            return TaotaoResult.ok();
        } catch (Exception e) {
            return TaotaoResult.error("导入失败");
            //return null;
        }
    }

}
