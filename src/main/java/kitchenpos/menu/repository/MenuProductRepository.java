package kitchenpos.menu.repository;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long id);
}
