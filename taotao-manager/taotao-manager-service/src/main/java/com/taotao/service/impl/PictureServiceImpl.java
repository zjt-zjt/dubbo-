package com.taotao.service.impl;

import com.taotao.common.ExceptionUtil;
import com.taotao.common.FtpUtil;
import com.taotao.common.IDUtils;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@PropertySource("classpath:/resources.properties")
public class PictureServiceImpl implements PictureService {

    @Value("${ftp.host}")
    private String ftpHost;
    @Value("${ftp.port}")
    private Integer ftpPort;
    @Value("${ftp.username}")
    private String ftpUserName;
    @Value("${ftp.password}")
    private String ftpPassword;
    @Value("${ftp.basePath}")
    private String ftpBasePath;
    @Value("${image.domain}")
    private String imageDomain;





    @Override
    public Map<String, Object> uploadPicture(MultipartFile picFile) {
        Map<String, Object> result = new HashMap<>();
        // 获取上传的文件后缀名
        String suffixName = picFile.getOriginalFilename().substring(picFile.getOriginalFilename().lastIndexOf('.'));
        // 将图片上传到FTP服务器
        String fileName = IDUtils.genImageNameByUUID() + suffixName;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        String filePath = sdf.format(new Date());

        StringBuilder fileUrl = new StringBuilder();
        fileUrl.append(imageDomain);
        fileUrl.append("/");
        fileUrl.append(filePath);
        fileUrl.append("/");
        fileUrl.append(fileName);

        // 返回图片访问地址


        // 上传插件支持的返回类型
        // //成功时
        //{
        //        "error" : 0,
        //        "url" : "http://www.example.com/path/to/file.ext"
        //}
        ////失败时
        //{
        //        "error" : 1,
        //        "message" : "错误信息"
        //}
        try {
            boolean flag = FtpUtil.uploadFile(ftpHost, ftpPort, ftpUserName, ftpPassword, ftpBasePath, filePath, fileName, picFile.getInputStream());
            if(!flag) {
                result.put("error", 1);
                result.put("message", "上传失败");
            } else {
                result.put("error", 0);
                result.put("url", fileUrl.toString());
            }
        } catch (IOException e) {
            result.put("error", 1);
            result.put("message", "上传失败: " + ExceptionUtil.getStackTrace(e));
        }

        return result;
    }
}
