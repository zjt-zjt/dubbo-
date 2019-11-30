package com.taotao.service.impl;

import com.taotao.common.ExceptionUtil;
import com.taotao.common.IDUtils;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.AddService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Slf4j
@Service
public class addServiceImpl  implements AddService {
    @Autowired(required = false)
    private TbItemMapper tbItemMapper;
    @Autowired(required = false)
    private TbItemDescMapper tbItemDescMapper;
    @Autowired(required = false)
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED
    )


    @Override
    public TaotaoResult saveItem(TbItem tbItem, String desc,String itemParams) {
        try {
            //不在利用随机生成的id
            //long itemId = IDUtils.genItemId();
            //tbItem.setId(itemId);
            tbItem.setStatus(SystemConstants.TAOTAO_ITEM_STATUS_NORMAL);
            tbItem.setCreated(new Date());
            tbItem.setUpdated(new Date());
            tbItemMapper.insert(tbItem);

            TbItemDesc tbItemDesc = new TbItemDesc();
            tbItemDesc.setItemDesc(desc);
            tbItemDesc.setCreated(new Date());
            tbItemDesc.setUpdated(new Date());
            //tbItemDesc.setItemId(itemId);
            tbItemDesc.setItemId(tbItem.getId());
            tbItemDescMapper.insert(tbItemDesc);

            TbItemParamItem itemParamItem = new TbItemParamItem();
           // itemParamItem.setItemId(itemId);
            itemParamItem.setItemId(tbItem.getId());
            itemParamItem.setParamData(itemParams);
            itemParamItem.setCreated(new Date());
            itemParamItem.setUpdated(new Date());
            tbItemParamItemMapper.insert(itemParamItem);

            return TaotaoResult.ok();
        } catch (Exception e) {
            log.error("保存商品失败", e);
            return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_ERROR, ExceptionUtil.getStackTrace(e));

        }

    }
}





