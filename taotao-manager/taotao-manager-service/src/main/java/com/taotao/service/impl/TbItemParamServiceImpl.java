package com.taotao.service.impl;


import com.github.pagehelper.PageHelper;
import com.taotao.common.ExceptionUtil;
import com.taotao.common.IDUtils;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.*;
import com.taotao.pojo.*;
import com.taotao.service.TbItemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TbItemParamServiceImpl implements TbItemParamService {

@Autowired(required = false)
private TbItemParamMapper tbItemParamMapper;
@Autowired(required = false)
private TbItemMapper tbItemMapper;
@Autowired(required = false)
private TbItemDescMapper tbItemDescMapper;
@Autowired(required = false)
private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired(required = false)
private TbItemCatMapper tbItemCatMapper;
//规格参数模板添加
    @Transactional
    @Override
    public TaotaoResult saveItemParam(Long itemCatId, String paramData) {
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setCreated(new Date());
        tbItemParam.setUpdated(new Date());
        tbItemParamMapper.insert(tbItemParam);
        return TaotaoResult.ok();
    }
//查询总数据
    @Override
    public List<TbItemParam> itemParamService() {
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExample(null);
        return tbItemParams;
    }
//分页查询(在TbItemParam中加入ItemCatName属性)
    @Override
    public List<TbItemParam> itemParamPageService(int page, int row) {
        PageHelper.startPage(page, row);
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        for(TbItemParam param:tbItemParams){
            Long itemCatId = param.getItemCatId();
            TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(itemCatId);
            param.setItemCatName(tbItemCat.getName());

        }
        return tbItemParams;
    }
//查询总条数
    @Override
    public long itemParamCountService() {
        long count = tbItemParamMapper.countByExample(null);
        return count;
    }
//新增商品套用模板动态生成规格参数表单
    @Override
    public TaotaoResult getItemParamByItemCatId(Long id) {
        TbItemParamExample condition = new TbItemParamExample();
        condition.createCriteria().andItemCatIdEqualTo(id);
        List<TbItemParam> tbItemParamList = tbItemParamMapper.selectByExampleWithBLOBs(condition);
        if(tbItemParamList.size() > 0) {
            return TaotaoResult.ok(tbItemParamList.get(0));
        }
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_ERROR, "未查询到数据");

    }

    //删除规格参数
    @Override
    public TaotaoResult delete(String id) {
        tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);
    }
    //规格参数的回显
    @Override
    public TaotaoResult getItemParamItem(Long id) {
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        tbItemParamItemExample.createCriteria().andItemIdEqualTo(id);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
        if(tbItemParamItems.size()>0){
            return TaotaoResult.ok(tbItemParamItems.get(0));
        }
        //TaotaoResult taotaoResult = new TaotaoResult(tbItemParamItems.get(0));
        //return taotaoResult;
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_ERROR, "未查询到数据");
    }
     //描述的回显
    @Override
    public TaotaoResult getItemParamItemDesc(Long id) {
        TbItemDescExample tbItemDescExample = new TbItemDescExample();
        tbItemDescExample.createCriteria().andItemIdEqualTo(id);
        List<TbItemDesc> tbItemDescs = tbItemDescMapper.selectByExampleWithBLOBs(tbItemDescExample);
        if(tbItemDescs.size()>0){
            return TaotaoResult.ok(tbItemDescs.get(0));
        }
        //TaotaoResult taotaoResult = new TaotaoResult(tbItemDescs.get(0));
        //return taotaoResult;
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_ERROR, "未查询到数据");
    }
     //编辑规格参数
    @Override
    public TaotaoResult EditItemParam(Long itemCatId, String paramData) {
         TbItemParam  tbItemParam = new TbItemParam();
             tbItemParam.setParamData(paramData);
             tbItemParam.setUpdated(new Date());
             TbItemParamExample tbItemParamExample = new TbItemParamExample();
             tbItemParamExample.createCriteria().andItemCatIdEqualTo(itemCatId);
        //List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);

        tbItemParamMapper.updateByExampleSelective(tbItemParam,tbItemParamExample);
        //tbItemParamMapper.updateByExampleWithBLOBs(tbItemParam,tbItemParamExample);
//        for( TbItemParam tbItemParam1 :tbItemParams){
//            tbItemParam1.setParamData(paramData);
//            tbItemParam1.setUpdated(new Date());
//            tbItemParamMapper.updateByPrimaryKeyWithBLOBs(tbItemParam1);
//        }

                return TaotaoResult.ok();
    }


}








