package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;

public class FakeProductDao implements ProductDao {

    private final List<Product> IN_MEMORY_PRODUCT;
    private Long id;

    public FakeProductDao() {
        IN_MEMORY_PRODUCT = new ArrayList<>();
        id = 1L;
    }

    @Override
    public Product save(Product entity) {
        Product product = new Product(id++, entity.getName(), entity.getPrice());
        IN_MEMORY_PRODUCT.add(product);
        return product;
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
