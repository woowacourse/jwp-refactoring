package kitchenpos.repository;

import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query("SELECT mp FROM MenuProduct mp WHERE mp.menu.id IN :ids")
    List<MenuProduct> findAllByMenuIds(@Param("ids") List<Long> ids);
}
