package kitchenpos.common.repository.menu;

import java.util.List;
import kitchenpos.common.domain.menu.MenuProduct;
import org.springframework.data.repository.Repository;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {

    MenuProduct save(MenuProduct entity);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
