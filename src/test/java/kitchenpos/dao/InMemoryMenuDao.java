package kitchenpos.dao;

import java.util.List;
import java.util.function.BiConsumer;
import kitchenpos.domain.Menu;

public class InMemoryMenuDao extends InMemoryAbstractDao<Menu> implements MenuDao {

    @Override
    protected BiConsumer<Menu, Long> setId() {
        return Menu::setId;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return ids.stream()
                .distinct()
                .filter(database::containsKey)
                .count();
    }
}
