package com.yjp.erp.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 附件上传cloud请求配置参数
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/16
 */
@Configuration
public class BillAttachmentProperties {

    @Value("${file.project.name}")
    private String projectName;

    @Value("${file.download-suffix}")
    private String downloadsuffix;

    @Value("${file.private-key}")
    private String privateKey;

    @Value("${file.public-key}")
    private String publicKey;

    @Value("${file.env}")
    private String env;

    @Value("${file.bucketname}")
    private String bucketName;

    @Value("${file.proxy-suffix}")
    private String proxySuffix;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDownloadsuffix() {
        return downloadsuffix;
    }

    public void setDownloadsuffix(String downloadsuffix) {
        this.downloadsuffix = downloadsuffix;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getProxySuffix() {
        return proxySuffix;
    }

    public void setProxySuffix(String proxySuffix) {
        this.proxySuffix = proxySuffix;
    }

    @Override
    public String toString() {
        return "BillAttachmentProperties{" +
                "projectName='" + projectName + '\'' +
                ", downloadsuffix='" + downloadsuffix + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", env='" + env + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", proxySuffix='" + proxySuffix + '\'' +
                '}';
    }
}