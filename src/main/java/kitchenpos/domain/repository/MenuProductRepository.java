package kitchenpos.domain.repository;

import java.util.List;
import kitchenpos.domain.menu.MenuProduct;
import org.springframework.data.repository.Repository;

public interface MenuProductRepository extends Repository<MenuProduct, Long> {

    MenuProduct save(MenuProduct entity);

    List<MenuProduct> findAll();

    List<MenuProduct> findAllByMenuId(Long menuId);
}
