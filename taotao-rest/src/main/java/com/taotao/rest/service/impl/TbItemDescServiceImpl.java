package com.taotao.rest.service.impl;

import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.pojo.TbItemDesc;
import com.taotao.rest.service.TbItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbItemDescServiceImpl implements TbItemDescService {
    @Autowired(required = false)
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TaotaoResult getItemDesc(Long id) {
        try {
            TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
            return TaotaoResult.ok(tbItemDesc);
        } catch (Exception e) {
            return TaotaoResult.error("查询商品描述信息失败");
        }
    }
}
