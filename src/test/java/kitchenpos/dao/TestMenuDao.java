package kitchenpos.dao;

import java.util.Comparator;
import java.util.function.BiConsumer;
import kitchenpos.domain.Menu;

public class TestMenuDao extends TestAbstractDao<Menu> implements MenuDao {
    @Override
    protected BiConsumer<Menu, Long> setIdConsumer(){
        return Menu::setId;
    }

    @Override
    protected Comparator<Menu> comparatorForSort() {
        return Comparator.comparingLong(Menu::getId);
    }
}
