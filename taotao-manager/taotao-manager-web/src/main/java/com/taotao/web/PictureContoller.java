package com.taotao.web;

import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class PictureContoller {

    @Autowired
    private PictureService pictureService;
//图片上传
    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map<String, Object> uploadPic(@RequestParam("uploadFile") MultipartFile picFile) {
        Map<String, Object> result = pictureService.uploadPicture(picFile);
        return result;
    }

}
