package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class MenuFakeDao implements MenuDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, Menu> repository = new HashMap<>();

    @Override
    public Menu save(Menu entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(repository.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return repository.keySet().stream()
                .filter(ids::contains)
                .count();
    }
}
