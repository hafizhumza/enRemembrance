package com.en.remembrance.services;

import com.en.remembrance.properties.ApplicationProperties;
import com.en.remembrance.utilities.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Slf4j
@Service
public class FileService {

    private final ApplicationProperties applicationProperties;

    private static Path filesStorageLocation;

    @Autowired
    public FileService(ApplicationProperties applicationProperties) throws IOException {
        this.applicationProperties = applicationProperties;
        createFilesDir(applicationProperties);
    }

    private void createFilesDir(ApplicationProperties applicationProperties) throws IOException {
        filesStorageLocation = Paths.get(applicationProperties.getSavedImagesDir()).toAbsolutePath().normalize();
        Files.createDirectories(filesStorageLocation);
    }

    public String save(MultipartFile file) throws IOException {
        String fileName = Util.getRandomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        Path targetLocation = filesStorageLocation.resolve(fileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = Util.getRandomUUID().replace("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());

        File path = filesStorageLocation.resolve(fileName).toFile();
        path.createNewFile();
        FileOutputStream output = new FileOutputStream(path);
        output.write(file.getBytes());
        output.close();

        return fileName;
    }

    public String saveBase64File(String base64File, String type) throws IOException {
        if (!StringUtils.hasText(base64File))
            return null;

        byte[] fileBytes = Base64.decodeBase64(base64File);
        String fileName = Util.getRandomUUID() + "." + type;

        File path = filesStorageLocation.resolve(fileName).toFile();
        new FileOutputStream(path).write(fileBytes);
        return path.getPath();
    }

    public String convertToBase64(String filePath) throws IOException {
        if (!StringUtils.hasText(filePath))
            return null;

        Path path = Paths.get(filePath);
        return new String(Base64.encodeBase64(Files.readAllBytes(path)), StandardCharsets.UTF_8);
    }

    public String convertToBase64(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty())
            return null;

        return new String(Base64.encodeBase64(file.getBytes()), StandardCharsets.UTF_8);
    }

    public String getFileType(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    public void deleteIfExists(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

    public void deleteIfExists(List<String> paths) {
        if (!CollectionUtils.isEmpty(paths)) {
            for (String path : paths) {
                try {
                    this.deleteIfExists(path);
                } catch (IOException e) {
                    log.error("Exception while deleting file: ", e);
                }
            }
        }
    }
}
