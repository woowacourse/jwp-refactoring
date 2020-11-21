package kitchenpos.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kitchenpos.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @Query("SELECT mp FROM MenuProduct mp WHERE mp.menu.id = :menuId")
    List<MenuProduct> findAllBy(@Param("menuId") Long menuId);
}
