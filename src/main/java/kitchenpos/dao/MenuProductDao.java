package kitchenpos.dao;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface MenuProductDao extends Repository<MenuProduct, Long> {
    MenuProduct save(MenuProduct entity);

    List<MenuProduct> findAll();

    @Query(value = "SELECT * FROM MenuProduct WHERE menu_id = ?0", nativeQuery = true)
    List<MenuProduct> findAllByMenuId(@Param("menuId") Long menuId);
}
