package kitchenpos.fake;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMenuGroupDao implements MenuGroupDao {

    private final Map<Long, MenuGroup> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public MenuGroup save(MenuGroup entity) {
        long id = this.id.getAndIncrement();
        entity.setId(id);
        map.put(id, entity);
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
        return map.values().stream()
                .anyMatch(it -> it.getId().equals(id));
    }
}
