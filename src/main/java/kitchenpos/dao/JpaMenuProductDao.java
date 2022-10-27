package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuProductDao extends JpaRepository<MenuProduct, Long>, MenuProductDao {
}
