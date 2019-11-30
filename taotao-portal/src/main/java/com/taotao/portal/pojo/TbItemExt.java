package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;

public class TbItemExt extends TbItem {
    public String[] getImages() {
        if (this.getImage() != null && this.getImage() != "") {
            String[] strings = this.getImage().split(",");
            return strings;
        }
        return null;
    }
}
