package kitchenpos.fakedao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class ProductFakeDao implements ProductDao {

    private Long autoIncrementId = 0L;
    private final Map<Long, Product> repository = new HashMap<>();

    @Override
    public Product save(Product entity) {
        repository.putIfAbsent(++autoIncrementId, entity);
        entity.setId(autoIncrementId);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(repository.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(repository.values());
    }
}
