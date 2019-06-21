package com.yjp.erp.util;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ConnectException;

@Component
public class FtpClientUtil {
    /**
     * ftp服务器地址
     */
    private static String host;
    /**
     * 端口
     */
    private static int port;
    /**
     * 用户名
     */
    private static String username;
    /**
     * 密码
     */
    private static String password;
    /**
     * ftp 客户端
     */
    private static FTPClient ftpClient;

    @Value("${moqui.server.host}")
    public void setHost(String host) {
        FtpClientUtil.host = host;
    }

    @Value("${moqui.server.port}")
    public void setPort(int port) {
        FtpClientUtil.port = port;
    }

    @Value("${moqui.server.username}")
    public void setUsername(String username) {
        FtpClientUtil.username = username;
    }

    @Value("${moqui.server.password}")
    public void setPassword(String password) {
        FtpClientUtil.password = password;
    }

//    static {
//        try {
//            initFtpClient();
//        } catch (Exception e) {
//            throw new RuntimeException(e + "  host:" + host + "  port:" + port + " 初始化FtpClient失败");
//        }
//    }


    public static void initFtpClient() throws Exception {
        FTPClient ftp = new FTPSClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
        //连接ftp服务器
        connect(ftp);
        //设置属性
        setProperty(ftp);
        ftpClient = ftp;
    }

    /**
     * 创建文件夹
     *
     * @param fileName 远程文件名
     */
    public static void remoteWriteFile(String fileName,String content) throws Exception {
        if (null == ftpClient || !ftpClient.isAvailable()) {
            initFtpClient();
        }
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
        ftpClient.storeFile(new String(fileName.getBytes("utf-8"),"iso-8859-1"),input);
    }

    /**
     * @param ftp
     * @throws Exception
     */
    private static void setProperty(FTPClient ftp) throws Exception {
        ftp.enterLocalPassiveMode();
        //二进制传输,默认为ASCII
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
    }

    /**
     * @param ftp
     * @param remoteFileName
     * @param locaFileName
     */
    private void upload(FTPClient ftp, String remoteFileName,
                        String locaFileName) throws Exception {
        //上传
        InputStream input;

        input = new FileInputStream(locaFileName);

        ftp.storeFile(remoteFileName, input);

        input.close();
    }

    /**
     * @param ftp
     */
    private static void connect(FTPClient ftp) throws Exception {
        //连接服务器
        ftp.connect(host, port);
        int reply = ftp.getReplyCode();
        //是否连接成功
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new ConnectException(host + " 服务器拒绝连接");
        }
        //登陆
        if (!ftp.login(username, password)) {
            throw new ConnectException("用户名或密码错误");
        }
    }
}