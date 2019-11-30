package com.taotao.rest.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.rest.service.TbItemParamItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {

    @Autowired(required = false)
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TaotaoResult getItemParam(Long id) {

        TbItemParamItem tbItemParamItem = null;
        try {
            TbItemParamItemExample condition = new TbItemParamItemExample();
            condition.createCriteria().andItemIdEqualTo(id);

            List<TbItemParamItem> tbItemParamItemList = tbItemParamItemMapper.selectByExampleWithBLOBs(condition);
            if(tbItemParamItemList == null || tbItemParamItemList.size() < 1) {
                log.warn("没有查询到ID为: " + id+"的商品规格参数信息");
                return TaotaoResult.error("没有查询到ID为: " + id+"的商品规格参数信息");
            }
            tbItemParamItem = tbItemParamItemList.get(0);
            return TaotaoResult.ok(tbItemParamItem);
        } catch (Exception e) {
            return TaotaoResult.error("获取商品规格参数失败");
        }
    }
}
