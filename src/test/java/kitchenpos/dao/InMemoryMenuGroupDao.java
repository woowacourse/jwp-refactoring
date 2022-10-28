package kitchenpos.dao;

import java.util.function.BiConsumer;
import kitchenpos.domain.MenuGroup;

public class InMemoryMenuGroupDao extends InMemoryAbstractDao<MenuGroup> implements MenuGroupDao {

    @Override
    protected BiConsumer<MenuGroup, Long> setId() {
        return MenuGroup::setId;
    }

    @Override
    public boolean existsById(final Long id) {
        return database.containsKey(id);
    }
}
