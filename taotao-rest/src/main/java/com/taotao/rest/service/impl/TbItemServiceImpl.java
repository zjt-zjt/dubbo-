package com.taotao.rest.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.rest.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired(required = false)
    private TbItemMapper tbItemMapper;

    @Override
    public TaotaoResult getItemDetails(Long id) {
        try {
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
            return TaotaoResult.ok(tbItem);
        } catch (Exception e) {
            return TaotaoResult.error("查询商品详情出错");
        }
    }
}
