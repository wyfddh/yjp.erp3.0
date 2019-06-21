package com.yjp.erp.service;

import com.yjp.erp.util.HttpClientUtils;
import com.yjp.erp.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BaseService {
    @Value("${xml.create.path}")
    private String xmlPath;

    @Value("${xml.zip.name}")
    private String zipFileName;

    @Value("${xml.zip.out.path}")
    private String outPath;

    @Value("${xml.upload.url}")
    private String uploadUrl;

    @Value("${moqui.component.recover.url}")
    private String recoverUrl;


    /**
     * 生成的xml打成zip包
     * @throws Exception
     */
    public void packageAllXml() throws Exception {
        File file=new File(outPath);
        if(!file.exists()){
            file.mkdirs();
        }
        ZipUtil.toZip(xmlPath, outPath+"/"+zipFileName+".zip", zipFileName, true);
    }

    /**
     * zip包上传到moqui 服务器
     * @throws Exception
     */
    public void uploadData() throws Exception {
        File uploadFile=new File(outPath+"/"+zipFileName+".zip");
        HttpEntity entity=new FileEntity(uploadFile);
        Map<String,String> headers=new HashMap<>();
        headers.put("zipFileName",zipFileName+".zip");
        HttpClientUtils.post(uploadUrl,entity,headers,2*1000,2*1000);
    }

    /**
     * 解压服务器的压缩包替换原有的component
     * @throws Exception
     */
    public void recoverComponent() throws Exception {
        Map<String,String> headers=new HashMap<>();
        headers.put("fileName",zipFileName+".zip");
        HttpClientUtils.post(recoverUrl,null,headers,2*1000,2*1000);
    }
}
