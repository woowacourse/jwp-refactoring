package kitchenpos.dao;

import java.util.Comparator;
import java.util.function.BiConsumer;
import kitchenpos.domain.MenuGroup;

public class TestMenuGroupDao extends TestAbstractDao<MenuGroup> implements MenuGroupDao {
    @Override
    protected BiConsumer<MenuGroup, Long> setIdConsumer() {
        return MenuGroup::setId;
    }

    @Override
    protected Comparator<MenuGroup> comparatorForSort() {
        return Comparator.comparingLong(MenuGroup::getId);
    }
}
