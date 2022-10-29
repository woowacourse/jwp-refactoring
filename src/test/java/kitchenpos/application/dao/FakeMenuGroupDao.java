package kitchenpos.application.dao;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuGroupDao implements MenuGroupDao {

    private final Map<Long, MenuGroup> menuGroups = new HashMap<>();

    private long id = 1;

    @Override
    public MenuGroup save(final MenuGroup menuGroup) {
        final var newMenuGroup = assignId(menuGroup);
        menuGroups.put(newMenuGroup.getId(), newMenuGroup);
        return newMenuGroup;
    }

    private MenuGroup assignId(final MenuGroup menuGroup) {
        return new MenuGroup(
                id++,
                menuGroup.getName()
        );
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.ofNullable(menuGroups.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroups.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroups.containsKey(id);
    }

    public void clear() {
        menuGroups.clear();
    }
}
