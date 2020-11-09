package com.lightdevel.wephuot.moneymanagement.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Objects;

@Component
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    private static final String COVER_PHOTO_FILENAME = "cover.png";

    private String storageFilePath;

    public FileUtil(@Value("${storage.file.path}") String storageFilePath) {
        this.storageFilePath = Objects.requireNonNull(storageFilePath);
    }

    public String saveCoverPhoto(String tripId, String coverPhotoBase64Encoded) {
        File dir = new File(storageFilePath + File.separator + tripId);
        if (!dir.exists()){
            dir.mkdirs();
        }
        String filename = dir.getAbsolutePath() + File.separator + COVER_PHOTO_FILENAME;

        String usefulData = coverPhotoBase64Encoded.substring(coverPhotoBase64Encoded.indexOf(",") + 1);
        byte[] data = Base64.decodeBase64(usefulData);
        try (OutputStream stream = new FileOutputStream(filename)) {
            stream.write(data);
            return filename.substring(storageFilePath.length());
        } catch (IOException ex) {
            LOGGER.error("Error saving cover photo for {}", tripId);
            ex.printStackTrace();
        }
        return null;
    }
}
