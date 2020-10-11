package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class MockMenuGroupDao implements MenuGroupDao {

    private Map<Long, MenuGroup> menuGroups = new HashMap<>();
    private Long id = 1L;

    @Override
    public MenuGroup save(MenuGroup entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        menuGroups.put(entity.getId(), entity);
        return entity;
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
