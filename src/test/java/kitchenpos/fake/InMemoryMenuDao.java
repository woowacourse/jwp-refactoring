package kitchenpos.fake;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMenuDao implements MenuDao {

    private final Map<Long, Menu> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Menu save(Menu entity) {
        long id = this.id.getAndIncrement();
        entity.setId(id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return map.values().stream()
                .filter(it -> ids.contains(it.getId()))
                .count();
    }
}
