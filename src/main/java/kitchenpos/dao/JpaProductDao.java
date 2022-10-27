package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductDao extends JpaRepository<Product, Long>, ProductDao {
}
