package org.example.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void savePhoto(MultipartFile photo, String photoName, String packageName);

    void deletePhoto(String imageUrl, String packageName);
}
