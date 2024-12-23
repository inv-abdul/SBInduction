/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.artem.sblearn.service.impl;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import com.artem.sblearn.entity.Products;
import com.artem.sblearn.repository.ProductsRepository;
import com.artem.sblearn.service.ProductService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.InputStream;

/**
 *
 * @author artem.valerievich
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final S3Client s3Client;

    @Value("${BUCKET_NAME}")
    private String S3bucket;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Value("${IMPORT_PATH}")
    private String importPath;

    @Autowired
    public ProductServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Autowired
    private ProductsRepository productRepository;

    @Override
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Products> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {

            String filepath = uploadPath + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(S3bucket)
                    .key(filepath)
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());

            s3Client.putObject(putObjectRequest, requestBody);
            return "File uploaded successfully";
        } catch (Exception e) {
            return "Error uploading file: " + e.getMessage();
        }
    }

    @Override
    public String ImportData(String fileName) {
        try {
            String filepath = importPath + fileName;
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3bucket)
                    .key(filepath)
                    .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());

            List<Products> products = new ArrayList<>();

            // Determine file type based on extension
            if (fileName.toLowerCase().endsWith(".csv")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                CSVParser csvParser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader);

                for (CSVRecord record : csvParser) {
                    Products product = new Products();
                    product.setName(record.get("name"));
                    product.setPrice(Double.parseDouble(record.get("price")));
                    product.setQuantity(Integer.parseInt(record.get("quantity")));
                    products.add(product);
                }
            } else if (fileName.toLowerCase().endsWith(".xlsx")) {
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                // Assuming first row is header
                for (Row row : sheet) {
                    // Skip header row
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    Products product = new Products();
                    product.setName(getStringCellValue(row.getCell(0))); // Assuming name is in first column
                    product.setPrice(getNumericCellValue(row.getCell(1))); // price in second column
                    product.setQuantity((int) getNumericCellValue(row.getCell(2))); // quantity in third column
                    products.add(product);
                }
                workbook.close();
            } else if (fileName.toLowerCase().endsWith(".xls")) {
                Workbook workbook = new HSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                // Assuming first row is header
                for (Row row : sheet) {
                    // Skip header row
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    Products product = new Products();
                    product.setName(getStringCellValue(row.getCell(0))); // Assuming name is in first column
                    product.setPrice(getNumericCellValue(row.getCell(1))); // price in second column
                    product.setQuantity((int) getNumericCellValue(row.getCell(2))); // quantity in third column
                    products.add(product);
                }
                workbook.close();
            } else {
                return "Unsupported file type. Please use CSV, XLS, or XLSX";
            }

            productRepository.saveAll(products);
            return "File processed and data inserted successfully";
        } catch (Exception e) {
            return "Error processing file: " + e.getMessage();
        }
    }

// Helper methods to safely read cell values
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0.0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return Double.parseDouble(cell.getStringCellValue().trim());
            default:
                return 0.0;
        }
    }

}
