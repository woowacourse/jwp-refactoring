package kitchenpos.infrastructure;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupDao;

public class MenuGroupFakeDao extends BaseFakeDao<MenuGroup> implements MenuGroupDao {

    @Override
    public boolean existsById(final Long id) {
        return entities.containsKey(id);
    }
}
