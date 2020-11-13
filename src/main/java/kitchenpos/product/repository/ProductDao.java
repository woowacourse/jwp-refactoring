package kitchenpos.product.repository;

import kitchenpos.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {

}
