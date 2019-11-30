package com.taotao.portal.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.pojo.TbItem;

import java.util.Date;

public class Item extends TbItem {
    @JsonIgnore
    public String[] getImages() {
        if (this.getImage() != null && this.getImage() != "") {
            String[] strings = this.getImage().split(",");
            return strings;
        }
        return null;
    }
  }




