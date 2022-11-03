package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.id in :productIds")
    List<Product> findByIds(List<Long> productIds);
}
