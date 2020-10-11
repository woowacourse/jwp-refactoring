package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class MockProductDao implements ProductDao {

    private Map<Long, Product> products = new HashMap<>();
    private Long id = 1L;

    @Override
    public Product save(Product entity) {
        if (Objects.isNull(entity.getId())) {
            entity.setId(id++);
        }
        products.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}
