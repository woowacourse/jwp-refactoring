package kitchenpos.product.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
}
