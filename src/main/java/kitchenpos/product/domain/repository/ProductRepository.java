package kitchenpos.product.domain.repository;

import java.util.List;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select count(p) from Product p where p.id in :productIds")
    long countProductInIds(List<Long> productIds);
}
