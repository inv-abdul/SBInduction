/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.request;
import com.artem.sblearn.validator.NotNull;
import com.artem.sblearn.validator.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.haseeb
 */
public class UploadRequest {
    @NotNull(message = "File must not be null or empty")
    @Size(max = 10 * 1024 * 1024, message = "File size must be less than 10MB")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
