package kitchenpos.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
