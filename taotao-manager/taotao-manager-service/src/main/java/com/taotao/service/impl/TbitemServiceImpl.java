package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.taotao.common.IDUtils;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import com.taotao.service.ITbitemService;
import jdk.net.SocketFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbitemServiceImpl implements ITbitemService {


    @Autowired(required = false)
    private TbItemMapper tbItemMapper;
    @Autowired(required = false)
    private TbItemDescMapper tbItemDescMapper;
    @Autowired(required = false)
    private TbItemParamItemMapper tbItemParamItemMapper;
//查询商品的所有数据
    @Override
    public List<TbItem> itemAllService() {
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        return tbItems;
    }
////查询状态1和2的商品
    @Override
    public List<TbItem> itemPageService(int page, int row) {
        PageHelper.startPage(page, row);
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
         criteria.andStatusBetween(Byte.parseByte("1"),Byte.parseByte("2"));
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
//        System.out.println(tbItems.size());

        return tbItems;
    }
//查询总条数
    @Override
    public long itemCountService() {
        long count = tbItemMapper.countByExample(null);
        return count;
    }
//根据状态进行更新,上下架
    @Override
    public TaotaoResult update(String id ,String status) {
      TbItem tbItem = new TbItem();
       tbItem.setId(Long.parseLong(id));
       tbItem.setStatus(Byte.parseByte(status));
        tbItemMapper.updateByPrimaryKeySelective(tbItem);
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);
    }
//编辑商品
    @Override
    public TaotaoResult saveItem(TbItem tbItem, String desc,String itemParams) {

        long itemId = tbItem.getId();
        tbItem.setId(itemId);
        tbItem.setStatus(SystemConstants.TAOTAO_ITEM_STATUS_NORMAL);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        tbItemMapper.updateByPrimaryKey(tbItem);

        TbItemDescExample  tbItemDescExample = new TbItemDescExample();
        tbItemDescExample.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemDesc> bItemDescs = tbItemDescMapper.selectByExampleWithBLOBs(tbItemDescExample);
    for(TbItemDesc tbItemDesc:bItemDescs){
        //TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setItemId(itemId);
        tbItemDescMapper.updateByPrimaryKeyWithBLOBs(tbItemDesc);
    }




        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        tbItemParamItemExample.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
        for(TbItemParamItem itemParamItem :tbItemParamItems){
            //TbItemParamItem itemParamItem = new TbItemParamItem();
            itemParamItem.setItemId(itemId);
            itemParamItem.setParamData(itemParams);
            itemParamItem.setCreated(new Date());
            itemParamItem.setUpdated(new Date());
            tbItemParamItemMapper.updateByPrimaryKeyWithBLOBs(itemParamItem);
        }

             return TaotaoResult.ok();


    }


}
