package kitchenpos.repository;

import java.util.List;
import java.util.Set;
import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long menuId);

    List<MenuProduct> findAllByMenuIdIn(Set<Long> menuIds);
}
