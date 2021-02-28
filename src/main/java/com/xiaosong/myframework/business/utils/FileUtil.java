package com.xiaosong.myframework.business.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtil {

    /**
     * 通过创建临时文件，实现转换
     * @param multipartFile
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            assert originalFilename != null;
            int lastIndex = originalFilename.lastIndexOf('.');
            if(lastIndex == -1 || lastIndex == originalFilename.length() - 1) {
                file=File.createTempFile(originalFilename, null);
            } else {
                file=File.createTempFile(originalFilename.substring(0, lastIndex), originalFilename.substring(lastIndex+1));
            }
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将inputStream转化为file
     * @param is
     * @param file 要输出的文件目录
     */
    public static void inputStream2File(InputStream is, File file) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];

            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } finally {
            os.close();
            is.close();
        }
    }
}
