package Baloot.Repository;

import Baloot.Model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Integer> {

    @Query("SELECT c FROM commodity c WHERE c.name = ?1")
    List<Commodity> findByName(String name);

    @Query("SELECT c FROM commodity c WHERE c.providerId = ?1")
    List<Commodity> findByProviderId(int provider);

    @Query("SELECT c FROM commodity c JOIN provider p ON c.providerId = p.id WHERE p.name = ?1")
    List<Commodity> findByProviderName(String providerName);

    @Query("SELECT c FROM commodity c WHERE c.categories IN ?1")
    List<Commodity> findByCategory(String category);
}
