package cocodas.prier.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //Product와 연관된 productMedia를 한 번의 쿼리로 함께 가져와서 N+1 문제 해결 (JOIN FETCH 사용해서 성능 개선)
    @Query("SELECT p FROM Product p JOIN FETCH p.productMedia")
    List<Product> findAllWithMedia();
}
