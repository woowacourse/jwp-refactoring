package kitchenpos.dao;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.domain.MenuProduct;

public class TestMenuProductDao extends TestAbstractDao<MenuProduct> implements MenuProductDao {

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return database.values().stream()
            .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
            .sorted(comparatorForSort())
            .collect(toList());
    }

    @Override
    protected BiConsumer<MenuProduct, Long> setIdConsumer() {
        return MenuProduct::setSeq;
    }

    @Override
    protected Comparator<MenuProduct> comparatorForSort() {
        return Comparator.comparingLong(MenuProduct::getMenuId);
    }
}
