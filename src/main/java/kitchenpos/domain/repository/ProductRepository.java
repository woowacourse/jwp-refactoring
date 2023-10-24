package kitchenpos.domain.repository;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIdIn(List<Long> productsIds);
}
