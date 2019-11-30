package com.taotao.web;

import com.taotao.common.FtpUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUpload {
    @Test
    public void testFtpClient() throws FileNotFoundException {

        String host = "images.taotao.com";
        int port = 21;
        String user = "ftpuser";
        String password = "zjtzjy137381";
        String basePath = "/home/ftpuser/images";
        String filePath = "20190726";
        String fileName = "aa.jpg";
        InputStream file = new FileInputStream("G:/11.jpg");

        boolean ret = FtpUtil.uploadFile(host, port, user, password, basePath, filePath, fileName, file);
        Assert.assertTrue("上传失败", ret);
        System.out.println(ret);
    }
}
