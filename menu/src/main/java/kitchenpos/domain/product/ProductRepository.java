package kitchenpos.domain.product;

import java.util.List;
import kitchenpos.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdIn(List<Long> ids);
}
