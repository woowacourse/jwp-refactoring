package kitchenpos.product.repository;

import java.util.List;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> productIds);
}
