package kitchenpos.fake;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.menu.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FakeMenuDao implements MenuDao {

    private Map<Long, Menu> menus = new HashMap<>();
    private Long id = 0L;

    @Override
    public Menu save(Menu entity) {
        if (entity.getId() != null) {
            menus.put(entity.getId(), entity);
            return entity;
        }
        entity.setId(++id);
        menus.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(menus.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(menus.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        Set<Long> savedIds = menus.keySet();
        return ids.stream()
                .filter(savedIds::contains)
                .count();
    }
}
