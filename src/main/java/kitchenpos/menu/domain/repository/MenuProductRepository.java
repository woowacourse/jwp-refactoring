package kitchenpos.menu.domain.repository;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    @Query("select mp from MenuProduct mp where mp.menu.id = :menuId")
    List<MenuProduct> findAllByMenuId(Long menuId);
}
