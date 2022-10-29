package kitchenpos.dao.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class FakeProductDao implements ProductDao {

    private long id = 0L;
    private final Map<Long, Product> products = new HashMap<>();

    @Override
    public Product save(final Product product) {
        long savedId = ++id;
        products.put(savedId, product);
        return new Product(savedId, product.getName(), product.getPrice());
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }
}
