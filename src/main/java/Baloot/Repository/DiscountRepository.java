package Baloot.Repository;

import Baloot.Model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    // Add custom repository methods if needed
}
