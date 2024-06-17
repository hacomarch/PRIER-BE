package cocodas.prier.point.kakao;

import cocodas.prier.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KakaoPayRepository extends JpaRepository<KakaoPay, Long> {
    List<KakaoPay> findKakaoPayByUsers(Users user);
}
