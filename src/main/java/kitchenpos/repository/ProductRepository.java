package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
