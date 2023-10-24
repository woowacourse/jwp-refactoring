package kitchenpos.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
