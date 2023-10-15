package kitchenpos.fake;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMenuGroupRepository implements MenuGroupRepository {

    private final Map<Long, MenuGroup> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public MenuGroup save(MenuGroup entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            MenuGroup menuGroup = new MenuGroup(id, entity.getName());
            map.put(id, menuGroup);
            return menuGroup;
        }
        map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public boolean existsById(Long id) {
        return map.containsKey(id);
    }

}
