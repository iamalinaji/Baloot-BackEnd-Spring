package Baloot.Repository;

import Baloot.Model.BuyItem;
import Baloot.Model.BuyItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyItemRepository extends JpaRepository<BuyItem, BuyItemId> {
    @Modifying
    @Query("DELETE FROM buy_item bi WHERE bi.commodity.id = ?1 AND bi.user.username = ?2")
    void deleteBuyItem(int commodityId, String username);
}
