package kitchenpos.util;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuGroupDao implements MenuGroupDao {
    private Long id = 0L;
    private final Map<Long, MenuGroup> repository = new HashMap<>();

    @Override
    public MenuGroup save(MenuGroup entity) {
        if (entity.getId() == null) {
            entity.setId(++id);
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfAbsent(entity.getId(), (id) -> entity);
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<MenuGroup> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean existsById(Long id) {
        return repository.containsKey(id);
    }
}
