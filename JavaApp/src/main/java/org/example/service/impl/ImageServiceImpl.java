package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.CantSaveFileException;
import org.example.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    /**
     * TODO
     * Костыль. Так как фото храняться в папке static, она компилируется и храниться в target->
     * при изменении во время работы программы ничего не подтягивается в target и используется старое изображение
     *  Решение в перспективе - использовать для хранения внешнюю папку
     *
     * @param photo       новое фото
     * @param photoName   название фото
     * @param packageName в какой папке
     */
    public void savePhoto(MultipartFile photo, String photoName, String packageName) {
        log.info("save image {}", photoName);
        String[] paths = {
            "src/main/resources/static/images/%s/".formatted(packageName),
            "target/classes/static/images/%s/".formatted(packageName)
        };

        for (String path : paths) {
            Path uploadPath = Paths.get(path);
            try {
                Files.createDirectories(uploadPath);
                Files.write(uploadPath.resolve(photoName), photo.getBytes());
                log.info("image {} saved success by path {}", photoName, path);
            } catch (IOException e) {
                log.error("image {} don't saved by path {}", photoName, path);
                throw new CantSaveFileException();
            }
        }
    }

    /**
     * TODO
     * Костыль. Так как фото храняться в папке static, она компилируется и храниться в target->
     * при изменении во время работы программы ничего не подтягивается в target и используется старое изображение
     *  Решение в перспективе - использовать для хранения внешнюю папку
     *
     * @param photoName   названия фото для пользователя
     * @param packageName в какой папке
     */
    public void deletePhoto(String photoName, String packageName) {
        String[] paths = {
            "src/main/resources/static/images/%s/".formatted(packageName),
            "target/classes/static/images/%s/".formatted(packageName)
        };

        for (String path : paths) {
            File directory = new File(path);
            if (!directory.exists() || !directory.isDirectory()) {
                continue;
            }

            File fileToDelete = new File(directory, photoName);
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                boolean deleted = fileToDelete.delete();
                if (deleted) {
                    log.info("Image {} deleted success", path);
                } else {
                    log.error("Image {} not deleted", path);
                }
            }
        }
    }
}

