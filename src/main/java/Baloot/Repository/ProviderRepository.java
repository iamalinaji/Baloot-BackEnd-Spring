package Baloot.Repository;

import Baloot.Model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    @Query("SELECT p FROM provider p WHERE p.name = ?1")
    List<Provider> findByName(String name);
}
