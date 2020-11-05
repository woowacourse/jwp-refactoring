package kitchenpos.dao;

import kitchenpos.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    int countAllByIdIn(List<Long> ids);
}
