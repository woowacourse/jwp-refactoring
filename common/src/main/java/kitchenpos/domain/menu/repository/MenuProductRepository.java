package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query("select mp from MenuProduct mp where mp.menu.id = :menuId")
    List<MenuProduct> findAllByMenuId(@Param("menuId") Long menuId);
}
