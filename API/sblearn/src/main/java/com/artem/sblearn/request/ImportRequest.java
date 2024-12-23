/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author abdul.haseeb
 */
public class ImportRequest {
    @NotNull(message = "File name is required")
    @NotEmpty(message = "File name should not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+\\.(csv|xls|xlsx)$", 
             message = "Invalid file name format")
    @Size(max = 100, message = "File name is too long")
    private String fileName;

    // Getters and setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
