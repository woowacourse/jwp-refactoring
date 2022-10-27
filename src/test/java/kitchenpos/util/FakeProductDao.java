package kitchenpos.util;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeProductDao implements ProductDao {

    private Long id = 0L;
    private final Map<Long, Product> repository = new HashMap<>();

    @Override
    public Product save(Product entity) {
        if (entity.getId() == null) {
            entity.setId(++id);
            repository.put(entity.getId(), entity);
            return entity;
        }
        return repository.computeIfAbsent(entity.getId(), (id) -> entity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        if (repository.containsKey(id)) {
            return Optional.of(repository.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return repository.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }
}
