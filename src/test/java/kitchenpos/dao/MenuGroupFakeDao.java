package kitchenpos.dao;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupFakeDao extends BaseFakeDao<MenuGroup> implements MenuGroupDao {

    @Override
    public boolean existsById(final Long id) {
        return entities.containsKey(id);
    }
}
