package com.taotao.portal.service.impl;

import com.taotao.common.HttpUtil;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItemDesc;
import com.taotao.portal.service.TbItemDescService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TbItemDescServiceImpl implements TbItemDescService {
    @Value("http://localhost:8081")
    private String restUrl;

    @Override
    public String getTbItemDesc(Long id) {
        String jsonResult = HttpUtil.doGet(restUrl + "/item/desc/"+id);
        TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonResult, TbItemDesc.class);
        if(taotaoResult.getStatus().equals(SystemConstants.TAOTAO_RESULT_STATUS_OK)) {
            TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
            return itemDesc.getItemDesc();
        } else {
            log.error("请求商品描述信息出错: " + taotaoResult.getMsg()+"\n异常：" + taotaoResult.getData());
            return "<span>暂无描述</span>";
        }
    }
}
