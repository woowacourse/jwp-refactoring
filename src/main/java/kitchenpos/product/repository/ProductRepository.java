package kitchenpos.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
