package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Hasee on 2019/12/10.
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
