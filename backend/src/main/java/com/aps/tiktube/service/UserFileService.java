package com.aps.tiktube.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aps.tiktube.model.Access;
import com.aps.tiktube.model.Entity;
import com.aps.tiktube.model.User;

public class UserFileService {
    private Path uploadPath;

    /**
     * Constructor for UserFileService
     *
     */
    public UserFileService(User user) {
        try {
            Path path = Paths.get("src/main/resources/static/" + user.getId());
            Files.createDirectories(path);
            this.uploadPath = path;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Upload a file
     * 
     * @return Path
     */
    private Path conflictSolve(MultipartFile file, Path path) {
        if (Files.exists(path)) {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                String[] name = fileName.split("\\.");
                String newName = name[0] + "_1." + name[1];
                return conflictSolve(file, Paths.get(path + "/" + newName));
            }
        }
        return path;
    }

    /**
     * Save the uploads
     * 
     * @return Path
     */
    public Path saveUploads(MultipartFile file) {
        try {
            Path path = Paths.get(uploadPath.toString() + "/" + file.getOriginalFilename());
            Path newPath = conflictSolve(file, path);
            Files.write(newPath, file.getBytes());
            return newPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unzip a file
     * 
     * @return Path
     */
    public Path unzip(Path path) {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            byte[] buffer = new byte[1024];
            File destFile = new File(uploadPath.resolve(Objects.requireNonNull(entry).getName()).toString());
            FileOutputStream fos = new FileOutputStream(destFile);
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            zipInputStream.closeEntry();
            return destFile.toPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Remove a file
     *
     */
    public static void removeFile(Path path) {
        try {
            if (Files.exists(path))
                Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove multiple files
     *
     */
    public static void removeMultipleFiles(List<Path> paths) {
        for (Path path : paths) {
            removeFile(path);
        }
    }

    /**
     * Get the file id
     * 
     * @return file id
     */
    public <T extends Entity<T>> String getFileId(MultipartFile file, T entity, String bucketName) {
        List<Path> pathList = new ArrayList<>();
        try {
            Path uploadFilePath = saveUploads(file).toAbsolutePath();
            Path unzipPath = unzip(uploadFilePath).toAbsolutePath();
            pathList.add(unzipPath);
            pathList.add(uploadFilePath);
    
            final String originalType = FilenameUtils.getExtension(unzipPath.getFileName().toString());
    
            final String fileName = unzipPath.getFileName().toString();
            final String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
    
            Access<T> entityAccess = new Access<>(entity.getTClass());
            InputStream is = Files.newInputStream(unzipPath);
    
            removeMultipleFiles(pathList);
    
            return entityAccess.importToGridFSFromInputStream(is,
                    entity.getId() + fileNameWithoutExtension.replace(" ", "_"), originalType, bucketName);
        } catch (IOException e) {
            e.printStackTrace();
            removeMultipleFiles(pathList);
            return "Error";
        }
    }
    

}
