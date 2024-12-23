/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.controller;

import com.artem.sblearn.entity.Products;
import com.artem.sblearn.request.ImportRequest;
import com.artem.sblearn.request.UploadRequest;
import com.artem.sblearn.response.MessageResponse;
import com.artem.sblearn.service.ProductService;
import com.artem.sblearn.validator.FileValidator;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author artem.valerievich
 */
@RestController
@Validated
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileValidator fileValidator;

    @GetMapping
    public List<Products> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable Integer id) {
        Optional<Products> product = productService.getProductById(id);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> uploadFile(@Valid @ModelAttribute UploadRequest request) {

        fileValidator.validateFile(request.getFile());
        String result = productService.uploadFile(request.getFile());
        return ResponseEntity.ok(new MessageResponse(result));
    }

    @GetMapping("/import")
    public ResponseEntity<MessageResponse> importData(@Valid @ModelAttribute ImportRequest request) {
        String result = productService.ImportData(request.getFileName());
        return ResponseEntity.ok(new MessageResponse(result));
    }

}
