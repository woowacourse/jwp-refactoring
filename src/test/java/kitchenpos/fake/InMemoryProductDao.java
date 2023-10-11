package kitchenpos.fake;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryProductDao implements ProductDao {
    private final Map<Long, Product> map = new HashMap<>();
    private AtomicLong id = new AtomicLong(0);

    @Override
    public Product save(Product entity) {
        long id = this.id.getAndIncrement();
        entity.setId(id);
        map.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(map.values());
    }
}
