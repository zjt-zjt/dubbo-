package com.taotao.portal.service.impl;

import com.taotao.common.HttpUtil;
import com.taotao.common.JsonUtils;
import com.taotao.common.SystemConstants;
import com.taotao.common.TaotaoResult;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.service.TbItemParamItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TbItemParamItemServiceImpl implements TbItemParamItemService {
    @Value("http://localhost:8081")
    private String restUrl;

    @Override
    public String getItemParam(Long id) {
        String jsonResult = HttpUtil.doGet(restUrl + "/item/param/" + id);
        TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonResult, TbItemParamItem.class);
        if (taotaoResult.getStatus().equals(SystemConstants.TAOTAO_RESULT_STATUS_OK)) {

            TbItemParamItem itemParam = (TbItemParamItem) taotaoResult.getData();
            if(!(itemParam==null)){
                String html = buildHtmlFromTbItemParamItem2(itemParam);
                return html;
            }

        }
        log.error("获取商品规格参数失败: " + taotaoResult.getMsg());
        return "<span>暂无商品规格参数<span>";
    }


    private String buildHtmlFromTbItemParamItem2(TbItemParamItem itemParamItem) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">");
        sb.append("    <tbody>");
        // 把数据库中存储的商品规格参数字符串转换成Java对象，方便下面组装html
        List<Map> paramDataItemList = JsonUtils.jsonToList(itemParamItem.getParamData(), Map.class);
        // 第一层循环是拼一个规格参数主体块的html
        for(Map itemParamMap : paramDataItemList) {
            String groupName = (String) itemParamMap.get("group");
            sb.append("        <tr>");
            sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+groupName+"</th>");
            sb.append("        </tr>");
            List<Map> params = (List<Map>) itemParamMap.get("params");
            // 第二层循环是拼接一个规格参数主体下面每一项规格参数的html
            for (Map map2 : params) {
                sb.append("        <tr>");
                sb.append("            <td class=\"tdTitle\">"+map2.get("k")+"</td>");
                sb.append("            <td>"+map2.get("v")+"</td>");
                sb.append("        </tr>");
            }
        }
        sb.append("    </tbody>");
        sb.append("</table>");
        return sb.toString();
    }
}
