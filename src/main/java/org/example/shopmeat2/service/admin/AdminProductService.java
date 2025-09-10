package org.example.shopmeat2.service.admin;

import org.example.shopmeat2.modals.Product;
import org.example.shopmeat2.repository.AdminProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminProductService {
    private final AdminProductRepository productRepository;

    public AdminProductService(AdminProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Product disableProduct(Integer id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setStatus((short) 3);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    @Transactional
    public Product restoreProduct(Integer id) {
        return productRepository.findById(id)
                .map(product -> {
                    // set lại trạng thái theo stockWeight
                    if (product.getStockWeight().compareTo(BigDecimal.ZERO) > 0) {
                        product.setStatus((short) 1); // còn hàng
                    } else {
                        product.setStatus((short) 2); // hết hàng
                    }
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }

    // Thêm sản phẩm mới
    @Transactional
    public Product addProduct(String productName, String category, BigDecimal price,
                              BigDecimal stockWeight, MultipartFile imageFile) throws IOException {
        if (productRepository.existsByProductNameIgnoreCase(productName)) {
            throw new RuntimeException("Tên sản phẩm đã tồn tại");
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setCategory(category);
        product.setPrice(price);
        product.setStockWeight(stockWeight);

        // Ẩn status theo stockWeight
        product.setStatus(stockWeight.compareTo(BigDecimal.ZERO) == 0 ? (short) 2 : (short) 1);

        if (imageFile != null && !imageFile.isEmpty()) {
            String extension = "";
            int dotIndex = imageFile.getOriginalFilename().lastIndexOf('.');
            if (dotIndex > 0) extension = imageFile.getOriginalFilename().substring(dotIndex);

            String uniqueFilename = System.currentTimeMillis() + extension;
            String uploadDir = "D:/Github Repositories/ShopMeatsSpringWork/images/";
            File dest = new File(uploadDir + uniqueFilename);
            dest.getParentFile().mkdirs(); // tạo folder nếu chưa có
            imageFile.transferTo(dest);

            product.setImageURL("/images/" + uniqueFilename);
        }


        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Integer id, String productName, BigDecimal price,
                                 BigDecimal stockWeight, MultipartFile imageFile) throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Kiểm tra tên mới có trùng không, nếu khác tên cũ
        if (!product.getProductName().equalsIgnoreCase(productName) &&
                productRepository.existsByProductNameIgnoreCase(productName)) {
            throw new RuntimeException("Tên sản phẩm đã tồn tại");
        }

        product.setProductName(productName);
        product.setPrice(price);
        product.setStockWeight(stockWeight);

        // Cập nhật status
        product.setStatus(stockWeight.compareTo(BigDecimal.ZERO) == 0 ? (short) 2 : (short) 1);

        // Cập nhật ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            String extension = "";
            int dotIndex = imageFile.getOriginalFilename().lastIndexOf('.');
            if (dotIndex > 0) extension = imageFile.getOriginalFilename().substring(dotIndex);

            String uniqueFilename = System.currentTimeMillis() + extension;
            String uploadDir = "D:/Github Repositories/ShopMeatsSpringWork/images/";

            File dest = new File(uploadDir + uniqueFilename);
            dest.getParentFile().mkdirs();
            imageFile.transferTo(dest);

            product.setImageURL("/images/" + uniqueFilename);
        }

        return productRepository.save(product);
    }

}


