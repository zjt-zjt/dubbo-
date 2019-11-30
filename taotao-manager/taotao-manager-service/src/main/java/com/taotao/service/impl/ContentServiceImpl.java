package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.EasyUIResult;
import com.taotao.common.HttpUtil;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired(required = false)
    private TbContentMapper contentMapper;
    //展示内容列表
    @Override
    public EasyUIResult getContentList(long catId, Integer page, Integer rows) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(catId);
        //分页处理
        PageHelper.startPage(page, rows);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        //取分页信息
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIResult result = new EasyUIResult(pageInfo.getTotal(), list);
        return result;
    }
//添加内容
    @Override
    public TaotaoResult addContent(TbContent content) {
        //把图片信息保存至数据库
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //把内容信息添加到数据库
        contentMapper.insert(content);
        try{
            Long contentCatId   =  content.getCategoryId();
            //HttpUtil.doGet("http://localhost:8081/cache/clear/content/cat/89");
            HttpUtil.doGet("http://localhost:8081/cache/clear/content/cat/"+contentCatId);

        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok();
        //return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);
    }

    //编辑内容
    @Override
    public TaotaoResult updateTbContent(TbContent tbContent, String desc) {
        Long id = tbContent.getId();
        tbContent.setId(id);
        tbContent.setUpdated(new Date());
        //tbContent.setCreated(new Date());
        contentMapper.updateByPrimaryKey(tbContent);
        try{
            Long contentCatId   =  tbContent.getCategoryId();
            //HttpUtil.doGet("http://localhost:8081/cache/clear/content/cat/89");
            HttpUtil.doGet("http://localhost:8081/cache/clear/content/cat/"+contentCatId);
        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok();
       // return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);
    }
//删除内容
    @Override
    public TaotaoResult delete(String id) {
        contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        try{
            HttpUtil.doGet("http://localhost:8081/cache/clear/content/cat/89");
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);
    }


}
