package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class InmemoryMenuGroupDao implements MenuGroupDao {

    private final Map<Long, MenuGroup> menuGroups;
    private long idValue;

    public InmemoryMenuGroupDao() {
        idValue = 0;
        menuGroups = new LinkedHashMap<>();
    }

    @Override
    public MenuGroup save(MenuGroup entity) {
        long savedId = idValue;
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(savedId);
        menuGroup.setName(entity.getName());
        menuGroups.put(savedId, menuGroup);
        idValue++;
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.ofNullable(menuGroups.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(menuGroups.values());
    }

    @Override
    public boolean existsById(Long id) {
        return menuGroups.containsKey(id);
    }
}
