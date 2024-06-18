package cocodas.prier.product;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.order.orderproduct.OrderProduct;
import cocodas.prier.order.orderproduct.OrderProductRepository;
import cocodas.prier.order.order.Orders;
import cocodas.prier.point.pointTransaction.PointTransactionService;
import cocodas.prier.point.pointTransaction.TransactionType;
import cocodas.prier.product.dto.ProductResponseDto;
import cocodas.prier.product.dto.ProductForm;
import cocodas.prier.product.media.ProductMedia;
import cocodas.prier.product.media.ProductMediaRepository;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final AwsS3Service awsS3Service;
    private final OrderProductRepository orderProductRepository;
    private final PointTransactionService pointTransactionService;
    private final UserRepository userRepository;


    @Transactional
    public String createProduct(ProductForm form, MultipartFile file) throws IOException {
        Product product = Product.builder()
                .productName(form.getProductName())
                .price(form.getPrice())
                .description(form.getDescription())
                .stock(form.getStock())
                .build();

        productRepository.save(product);

        String key = handleFileUpload(file);

        ProductMedia productMedia = ProductMedia.builder()
                .metadata(file.getOriginalFilename())
                .s3Key(key)
                .product(product)
                .build();
        productMediaRepository.save(productMedia);

        log.info("상품 등록 성공");
        return "상품 등록 성공";
    }

    private String handleFileUpload(MultipartFile file) throws IOException {

        /*
            임시 파일을 생성.
            첫 번째 인자는 파일의 접두어, 두 번째 인자는 접미어(파일 확장자를 포함한 파일 이름)로
            시스템의 기본 임시 파일 디렉터리에 저장.
        */
        File tempFile = File.createTempFile("product-", file.getOriginalFilename());
        /*
            사용자가 업로드한 파일(MultipartFile 객체)을 위에서 생성한 임시 파일로 복사.
            이 과정을 통해 서버에 파일이 저장.
         */
        file.transferTo(tempFile);

        String key = awsS3Service.uploadFile(tempFile);
        tempFile.delete();
        return key;
    }

    @Transactional
    public String deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        ProductMedia productMedia = productMediaRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품이미지"));
        awsS3Service.deleteFile(productMedia.getS3Key());
        productMediaRepository.delete(productMedia);

        productRepository.delete(product);
        log.info("상품 삭제 완료");
        return "상품 삭제 완료";
    }

    @Transactional
    public String updateProduct(Long productId, ProductForm form, MultipartFile newFile) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품"));

        product.changeProductName(form.getProductName());
        product.changePrice(form.getPrice());
        product.changeDescription(form.getDescription());
        product.changeStock(form.getStock());

        if (newFile != null && !newFile.isEmpty()) {
            ProductMedia media = productMediaRepository.findByProduct(product)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 상품 이미지"));

            awsS3Service.deleteFile(media.getS3Key());
            String newKey = handleFileUpload(newFile);
            media.changeMetadata(newFile.getOriginalFilename());
            media.changeS3Key(newKey);
        }

        log.info("상품 정보 업데이트 완료");
        return "상품 정보 업데이트 완료";
    }

    @Transactional
    public void createOrderProduct(Long userId, Product product, Orders order) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        Integer price = product.getPrice();

        // 구매 전 재고 확인
        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        // 포인트 차감
        pointTransactionService.decreasePoints(user, price, TransactionType.PRODUCT_PURCHASE);

        // 재고 감소
        product.changeStock(product.getStock() - 1);

        // 주문 저장
        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .orders(order)
                .price(price)
                .build();

        productRepository.save(product);
        orderProductRepository.save(orderProduct);
    }

    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAllWithMedia();
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품"));
        return convertToDto(product);
    }

    private ProductResponseDto convertToDto(Product product) {
        String imageUrl = awsS3Service.getPublicUrl(product.getProductMedia().getS3Key());
        return new ProductResponseDto(product.getProductId(),
                                    product.getProductName(),
                                    product.getPrice(),
                                    product.getDescription(),
                                    product.getStock(),
                                    imageUrl);
    }

}
