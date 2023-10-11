package kitchenpos.fake;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.*;

public class FakeMenuDao implements MenuDao {

    private static Map<Long, Menu> menus = new HashMap<>();
    private static Long id = 0L;

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
