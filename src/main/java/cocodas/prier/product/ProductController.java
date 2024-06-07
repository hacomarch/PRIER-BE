package cocodas.prier.product;

import cocodas.prier.orders.orders.OrderService;
import cocodas.prier.orders.orders.Orders;
import cocodas.prier.product.dto.ProductResponseDto;
import cocodas.prier.product.dto.ProductForm;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;


    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestPart("product") ProductForm form,
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
    public ResponseEntity<String> updateProduct(@PathVariable Long productId,
                                                @RequestPart("product") ProductForm form,
                                                @RequestParam(name = "file", required = false) MultipartFile file) {
        try {
            String result = productService.updateProduct(productId, form, file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("상품 정보 업데이트 실패");
        }
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public ProductResponseDto getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/purchase/{productId}")
    public ResponseEntity<String> purchaseProduct(@PathVariable Long productId,
                                                  @RequestHeader("Authorization") String auth,
                                                  @RequestParam Integer count) {
        String token = getToken(auth);
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);

        // 사용자 및 상품 조회
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 새로운 주문 생성
        Orders order = orderService.createOrder(users);

        // 상품 구매
        productService.createOrderProduct(userId, product, order);
        return ResponseEntity.ok("상품 구매가 완료되었습니다.");
    }
}
