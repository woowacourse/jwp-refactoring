package kitchenpos.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.product.domain.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

}
