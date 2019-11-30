package com.taotao.service.impl;


import com.taotao.common.EUTreeNode;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
   @Autowired(required = false)
    private TbContentCategoryMapper tbContentCategoryMapper;

    //展示内容分类列表
    @Override
    public List<EUTreeNode> getContentCategoryList(long parentid) {
        TbContentCategoryExample tbContentCategoryExample  = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
          criteria.andParentIdEqualTo(parentid);
          criteria.andStatusEqualTo(1);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
          List<EUTreeNode>    resultList = new ArrayList<>();
          for(TbContentCategory tbContentCategory :list){
               EUTreeNode node = new EUTreeNode();
               node.setId(tbContentCategory.getId());
               node.setText(tbContentCategory.getName());


              if (tbContentCategory.getIsParent()) {
                  node.setState("closed");
              } else {
                  node.setState("open");
              }
              resultList.add(node);
          }
               return   resultList;
          }
    //新增子节点
    @Override
    public TaotaoResult addNode(long parentId, String name) {
        Date date = new Date();
        //添加一个新节点
        //创建一个节点对象
        TbContentCategory node = new TbContentCategory();
        node.setName(name);
        node.setParentId(parentId);
        node.setIsParent(false);
        node.setCreated(date);
        node.setUpdated(date);
        node.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        node.setStatus(1);
        //插入新节点。需要返回主键
        tbContentCategoryMapper.insert(node);
        //判断如果父节点的isparent不是true修改为true
        //取父节点的内容
        TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parentNode.getIsParent()) {
            parentNode.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parentNode);
        }
        //把新节点返回
        return TaotaoResult.ok(node);
    }
    //重命名子节点
    @Override
    public TaotaoResult updateNode(long id, String name) {
        Date date = new Date();
        TbContentCategory node = new TbContentCategory();
        node.setName(name);
        node.setId(id);
        node.setUpdated(date);
        tbContentCategoryMapper.updateByPrimaryKeySelective(node);
        return TaotaoResult.build(SystemConstants.TAOTAO_RESULT_STATUS_OK, null);

}
    //删除子节点
   @Override
    public TaotaoResult deleteNode(long id ,long parentId) {
        Date date = new Date();
        TbContentCategory node =tbContentCategoryMapper.selectByPrimaryKey(id);
        node.setStatus(2);
        node.setUpdated(date);
        tbContentCategoryMapper.updateByPrimaryKey(node);
    //如果有子节点,先删除子节点
    if(node.getIsParent()){
        TbContentCategoryExample tbContentCategoryExample  = new TbContentCategoryExample();
        tbContentCategoryExample.createCriteria().andParentIdEqualTo(id);
        //获取所有的子节点
        List<TbContentCategory> sonList  = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
       for(TbContentCategory nodeList :sonList){
           //删除子节点
           nodeList.setStatus(2);
           nodeList.setUpdated(date);
           tbContentCategoryMapper.updateByPrimaryKey(nodeList);
       }
    }
      TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(node.getParentId());
       TbContentCategoryExample Example  = new TbContentCategoryExample();
       Example.createCriteria().andParentIdEqualTo(parentNode.getId());
       List<TbContentCategory> parentSonList  = tbContentCategoryMapper.selectByExample(Example);
     int count = 0;
     for(TbContentCategory parentSon:parentSonList){
         if(!parentSon.getId().equals(id)){
           if(parentSon.getStatus().equals(1)){
            count++;
           }
         }
     }
    if(count<=0){
        parentNode.setIsParent(false);
        tbContentCategoryMapper.updateByPrimaryKey(parentNode);
    }



        return TaotaoResult.ok();
    }


}


