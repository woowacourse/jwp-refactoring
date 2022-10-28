package kitchenpos.dao;

import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.domain.MenuProduct;

public class InMemoryMenuProductDao extends InMemoryAbstractDao<MenuProduct> implements MenuProductDao {


    @Override
    protected BiConsumer<MenuProduct, Long> setId() {
        return MenuProduct::setSeq;
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return null;
    }
}
