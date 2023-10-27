package kitchenpos.menugroup;

import domain.MenuGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import repository.MenuGroupDao;

public class InMemoryMenuGroupDao implements MenuGroupDao {

    private final List<MenuGroup> menuGroups = new ArrayList<>();

    @Override
    public MenuGroup save(final MenuGroup entity) {
        final var id = (long) (menuGroups.size() + 1);
        final var saved = new MenuGroup(id, entity.getName());
        menuGroups.add(saved);
        return saved;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return menuGroups.stream()
                         .filter(menuGroup -> menuGroup.getId().equals(id))
                         .findAny();
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroups;
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroups.stream()
                         .anyMatch(menuGroup -> menuGroup.getId().equals(id));
    }
}
