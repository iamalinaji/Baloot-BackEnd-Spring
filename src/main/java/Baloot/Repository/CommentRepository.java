package Baloot.Repository;

import Baloot.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM comment c WHERE c.commodityId = ?1")
    List<Comment> findByCommodityId(int commodityId);
}
