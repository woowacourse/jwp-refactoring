package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
}
