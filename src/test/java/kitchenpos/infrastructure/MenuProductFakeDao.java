package kitchenpos.infrastructure;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductDao;

public class MenuProductFakeDao extends BaseFakeDao<MenuProduct> implements MenuProductDao {

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return entities.values()
                .stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }

    @Override
    public void update(final MenuProduct entity) {
        entities.put(entity.getSeq(), entity);
    }
}
