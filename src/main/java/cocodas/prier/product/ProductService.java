package cocodas.prier.product;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.product.dto.CreateProductForm;
import cocodas.prier.product.media.ProductMedia;
import cocodas.prier.product.media.ProductMediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final AwsS3Service awsS3Service;

    @Transactional()
    public String createProduct(CreateProductForm form, MultipartFile file) throws IOException {
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

        // S3에서 관련 파일 삭제
        productMediaRepository.findByProduct(product).forEach(media -> {
            awsS3Service.deleteFile(media.getS3Key());
            productMediaRepository.delete(media); // DB에서 미디어 정보 삭제
        });

        productRepository.delete(product);
        log.info("상품 삭제 완료");
        return "상품 삭제 완료";
    }
}
