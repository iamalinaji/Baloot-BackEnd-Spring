package Baloot.Repository;

import Baloot.Model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Integer> {
    // Add any additional query methods if needed
}
