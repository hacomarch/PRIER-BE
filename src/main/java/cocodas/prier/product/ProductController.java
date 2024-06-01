package cocodas.prier.product;

import cocodas.prier.product.dto.CreateProductForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestPart("product") CreateProductForm form,
                                                @RequestParam("file") MultipartFile file) {
        try {
            String result = productService.createProduct(form, file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 등록 실패");
        }
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        String result = productService.deleteProduct(productId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<String> updateProduct
}


