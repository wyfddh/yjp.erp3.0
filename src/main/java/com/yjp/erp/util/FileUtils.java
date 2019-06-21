package com.yjp.erp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
* @Description: 文件写入工具
* @CreateDate: 2019/4/4 16:18
* @EMAIL: jianghongping@yijiupi.com
*/
public class FileUtils {
    /**
     * 字符串写入文件
     * @param filePath
     * @param fileName
     * @param fileContext
     */
    public static void writeToFile(String filePath, String fileName, String fileContext) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(file, fileName));
            writer.write(fileContext);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("写入xml文件失败！");
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
