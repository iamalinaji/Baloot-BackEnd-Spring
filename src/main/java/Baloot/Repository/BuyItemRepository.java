package Baloot.Repository;

import Baloot.Model.BuyItem;
import Baloot.Model.BuyItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyItemRepository extends JpaRepository<BuyItem, BuyItemId> {
    // Add custom repository methods if needed
}
