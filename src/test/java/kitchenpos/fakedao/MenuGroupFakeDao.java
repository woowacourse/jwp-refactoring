package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFakeDao implements MenuGroupDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, MenuGroup> repository = new HashMap<>();

    @Override
    public MenuGroup save(MenuGroup entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public boolean existsById(Long id) {
        return repository.containsKey(id);
    }
}
