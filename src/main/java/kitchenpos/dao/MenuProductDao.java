package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductDao extends JpaRepository<MenuProduct, Long> {
//    MenuProduct save(MenuProduct entity);
//
//    Optional<MenuProduct> findById(Long id);
//
//    List<MenuProduct> findAll();
//
    List<MenuProduct> findAllByMenuId(Long menuId);
}
