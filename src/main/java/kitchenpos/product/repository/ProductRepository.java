package kitchenpos.product.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
