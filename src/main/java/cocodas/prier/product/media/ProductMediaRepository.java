package cocodas.prier.product.media;

import cocodas.prier.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {
    Optional<ProductMedia> findByProduct(Product product);
}
