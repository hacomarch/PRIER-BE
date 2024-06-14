package cocodas.prier.board.post.like;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUsers(Users users);
    Likes findByPostAndUsers(Post post, Users users);
}
