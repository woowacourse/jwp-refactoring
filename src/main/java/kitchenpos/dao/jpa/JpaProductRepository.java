package kitchenpos.dao.jpa;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {
}
