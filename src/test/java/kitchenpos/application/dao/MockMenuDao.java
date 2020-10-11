package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class MockMenuDao implements MenuDao {

    private Map<Long, Menu> menus = new HashMap<>();
    private Long id = 1L;

    @Override
    public Menu save(Menu entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        menus.put(entity.getId(), entity);
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
        return menus.keySet().stream()
            .filter(ids::contains)
            .count();
    }
}
