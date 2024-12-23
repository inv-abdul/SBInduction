/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.artem.sblearn.service;

import com.artem.sblearn.entity.Products;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author artem.valerievich
 */
public interface ProductService {
    
    List<Products> getAllProducts();
    
    Optional<Products> getProductById(Integer id);
    
    String uploadFile(MultipartFile file);
    
    String ImportData(String fileName);
    
}
