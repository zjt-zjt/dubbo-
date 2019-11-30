package com.taotao.portal.service.impl;

import com.taotao.common.HttpUtil;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.TbItemExt;
import com.taotao.portal.service.TbItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TbItemServiceImpl implements TbItemService {
    @Value("http://localhost:8081")
    private String restUrl;

    @Override
    public TbItemExt getTbItemInfo(Long id) {
        String resultJson = HttpUtil.doGet(restUrl + "/item/get_item_detail/" + id);
        TaotaoResult taotaoResult = TaotaoResult.formatToPojo(resultJson, TbItem.class);
        if(taotaoResult.getStatus().equals(SystemConstants.TAOTAO_RESULT_STATUS_OK)) {
            TbItem tbItem = (TbItem) taotaoResult.getData();
            return parseTbItem2Ext(tbItem, new TbItemExt());
        }
        log.error("获取商品详情失败: " + taotaoResult.getMsg()+"\n异常栈信息：" + taotaoResult.getData());
        return null;
    }

    /**
     * 将TbItem对象转换成前端页面需要的TbItemExt对象
     * @param src
     * @param dst
     * @return
     */
    private TbItemExt parseTbItem2Ext(TbItem src, TbItemExt dst) {
        dst.setId(src.getId());
        dst.setCreated(src.getCreated());
        dst.setUpdated(src.getUpdated());
        dst.setStatus(src.getStatus());
        dst.setBarcode(src.getBarcode());
        dst.setCid(src.getCid());
        dst.setImage(src.getImage());
        dst.setPrice(src.getPrice());
        dst.setNum(src.getNum());
        dst.setTitle(src.getTitle());
        dst.setSellPoint(src.getSellPoint());
        return dst;
    }

}
