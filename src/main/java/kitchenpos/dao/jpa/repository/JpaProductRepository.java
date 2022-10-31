package kitchenpos.dao.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Product;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
}
