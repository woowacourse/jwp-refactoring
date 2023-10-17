package kitchenpos.domain;

import java.util.List;
import java.util.Optional;

public interface MenuProductRepository {

  MenuProduct2 save(MenuProduct2 menu);

  Optional<MenuProduct2> findById(Long id);

  List<MenuProduct2> findAll();

  List<MenuProduct2> findAllByMenuId(Long menuId);
}
