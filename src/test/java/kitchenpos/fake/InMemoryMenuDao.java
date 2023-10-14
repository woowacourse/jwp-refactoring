package kitchenpos.fake;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMenuDao implements MenuDao {

    private final Map<Long, Menu> map = new HashMap<>();
    private final AtomicLong id = new AtomicLong();

    @Override
    public Menu save(Menu entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.id.getAndIncrement();
            Menu menu = new Menu(id, entity.getName(), entity.getPrice(), entity.getMenuGroupId(), entity.getMenuProducts());
            map.put(id, menu);
            return menu;
        }
        map.put(entity.getId(), entity);
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
