/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.validator;
import jakarta.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author abdul.haseeb
 */
@Component
public class FileValidator {
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "text/csv", 
        "application/vnd.ms-excel", 
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    public void validateFile(MultipartFile file) {
        // Check if file is empty
        if (file == null || file.isEmpty()) {
            throw new ValidationException("File cannot be empty");
        }

        // Validate file content type
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new ValidationException("Invalid file type. Allowed types are: CSV, XLS, XLSX");
        }

        // Additional file name validation
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.contains("..")) {
            throw new ValidationException("Invalid file name");
        }
    }
}
