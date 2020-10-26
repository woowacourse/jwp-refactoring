package kitchenpos.dao;

import kitchenpos.domain.menu.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {
}
