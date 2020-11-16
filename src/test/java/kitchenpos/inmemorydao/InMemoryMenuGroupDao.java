package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class InMemoryMenuGroupDao implements MenuGroupDao {
    private Map<Long, MenuGroup> menuGroups;
    private long index;

    public InMemoryMenuGroupDao() {
        this.menuGroups = new HashMap<>();
        this.index = 0;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(key);
        menuGroup.setName(entity.getName());

        menuGroups.put(key, menuGroup);
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.ofNullable(menuGroups.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(menuGroups.values());
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroups.containsKey(id);
    }
}
