package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;

public class FakeProductDao implements ProductDao {

    private final List<Product> IN_MEMORY_PRODUCT;

    public FakeProductDao() {
        IN_MEMORY_PRODUCT = new ArrayList<>();
    }

    @Override
    public Product save(Product entity) {
        IN_MEMORY_PRODUCT.add(entity);
        Long id = (long) IN_MEMORY_PRODUCT.size();
        entity.setId(id);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return IN_MEMORY_PRODUCT.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(IN_MEMORY_PRODUCT);
    }
}
