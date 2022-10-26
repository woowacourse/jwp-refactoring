package kitchenpos.util;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeTableGroupDao implements TableGroupDao {

    private Long id = 0L;
    private final Map<Long, TableGroup> repository = new HashMap<>();

    @Override
    public TableGroup save(TableGroup entity) {
        if (entity.getId() == null) {
            entity.setId(++id);
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfAbsent(entity.getId(), (id) -> entity);
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<TableGroup> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
