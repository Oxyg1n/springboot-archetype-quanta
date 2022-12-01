package com.linine.archetype.utils;

import com.linine.archetype.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {
    @Autowired
    MD5Utils md5Utils;
    @Value("${project.file-path}")
    public String filePath;


    /**
     * 获得文件格式
     *
     * @param multipartFile 文件
     * @return
     */
    public String getFormat(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String format = fileName.substring(fileName.lastIndexOf("."));
        return format;
    }

    /**
     * 上传文件
     *
     * @param uploadFile 文件
     * @param format     文件格式
     * @return 文件新名称
     */
    private String uploadFile(MultipartFile uploadFile, String format, String path) {
        // 定位存放的文件夹
        File folder = new File(path);
        // 不存在则新建
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 文件夹更名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newName = uuid + format;
        File file = new File(folder, newName);

        // 文件保存
        try {
            uploadFile.transferTo(file);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("I/O错误");
        }
        return newName;
    }


    /**
     * 上传头像文件
     *
     * @param file 文件
     * @return 新文件名
     */
    public String uploadAvatar(MultipartFile file) {
        return uploadFile(file, getFormat(file), filePath + "avatar/");
    }

}
