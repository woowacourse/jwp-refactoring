package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @Query("SELECT mp FROM MenuProduct mp WHERE mp.menu.id = :menuId")
    List<MenuProduct> findAllByMenuId(@Param("menuId") Long menuId);
}
