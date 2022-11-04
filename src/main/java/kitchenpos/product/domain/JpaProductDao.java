package kitchenpos.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductDao extends JpaRepository<Product, Long>, ProductDao {
}
