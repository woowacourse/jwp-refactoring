package kitchenpos.dao;

import kitchenpos.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
