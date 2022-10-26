package kitchenpos.util;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuDao implements MenuDao {

    private Long id = 0L;
    private final Map<Long, Menu> repository = new HashMap<>();

    @Override
    public Menu save(Menu entity) {
        if (entity.getId() == null) {
            entity.setId(++id);
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfAbsent(entity.getId(), (id) -> entity);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Menu> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return repository.values()
                .stream()
                .filter(each -> ids.contains(each.getId()))
                .count();
    }
}
