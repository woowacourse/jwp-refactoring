package kitchenpos.dao;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductFakeDao extends BaseFakeDao<MenuProduct> implements MenuProductDao {

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return entities.values()
                .stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }
}
