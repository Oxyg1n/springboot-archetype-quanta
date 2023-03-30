package com.linine.archetype.utils;

import com.linine.archetype.constants.FilePathPrefix;
import com.linine.archetype.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class FileUtils {

    private final MD5Utils md5Utils;

    @Value("${project.file-path}")
    public String fileMainPath;

    public FileUtils(MD5Utils md5Utils) {
        this.md5Utils = md5Utils;
    }

    /**
     * 获取一个随机文件名
     */
    public String getRandomFileName() {
        String salt = md5Utils.getSalt();
        return System.currentTimeMillis() + salt;
    }

    /**
     * 获得文件md5
     *
     * @param file 文件
     * @return 文件md5
     */
    public String getFileMd5(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            log.warn(e.getClass() + "|" + e.getMessage() + "|" + e.getCause());
            throw new ApiException("计算文件MD5错误");
        }
    }

    /**
     * 获得文件格式
     *
     * @param multipartFile 文件
     * @return 文件格式
     */
    public String getFormat(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null) throw new ApiException("文件名称为空");
        return fileName.substring(fileName.lastIndexOf("."));
    }


    private void uploadFileWithFileName(MultipartFile uploadFile, String parentPath, String fileName) {
        // 定位存放的文件夹
        File folder = new File(parentPath);
        // 不存在则新建
        boolean exists = folder.exists();
        if (!exists) exists = folder.mkdirs();
        if (!exists) {
            log.warn("存放文件错误,创建文件夹失败:" + folder.getAbsolutePath());
            throw new ApiException("I/O错误");
        }
        // 文件夹更名
        File file = new File(folder, fileName);
        // 文件保存
        try {
            uploadFile.transferTo(file);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("I/O错误");
        }
    }

    /**
     * 上传文件
     * 先计算文件的MD5值，若已经存在相同MD5命名的文件则返回，否则写入后返回
     *
     * @return 次级存储路径+新文件名
     */
    private String uploadFile(MultipartFile file, String fileSubPath) {
        String md5 = getFileMd5(file);
        String format = getFormat(file);
        String storageFileName = md5 + format;
        if (!checkFileExist(fileSubPath + storageFileName))
            uploadFileWithFileName(file, fileMainPath + fileSubPath, storageFileName);
        return fileSubPath + storageFileName;
    }

    /**
     * 用户上传文件
     *
     * @param file 文件
     * @return
     */
    public String userUploadFile(MultipartFile file) {
        return uploadFile(file, FilePathPrefix.USER_UPLOAD_PREFIX);
    }

    /**
     * 用户上传头像文件
     *
     * @param file 文件
     * @return
     */
    public String userUploadAvatar(MultipartFile file) {
        return uploadFile(file, FilePathPrefix.USER_AVATAR_PREFIX);
    }


    /**
     * 根据次级名称+文件名判断文件是否存在
     *
     * @param fileNameWithSubPath 文件次级目录+文件名
     * @return
     */
    public boolean checkFileExist(String fileNameWithSubPath) {
        return new File(fileMainPath, fileNameWithSubPath).exists();
    }


}
