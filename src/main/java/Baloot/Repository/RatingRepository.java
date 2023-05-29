package Baloot.Repository;

import Baloot.Model.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Baloot.Model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    // You can add custom query methods or use the default methods provided by JpaRepository
}
