package kitchenpos.dao;

import static kitchenpos.application.fixture.ProductFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.domain.Product;

public class FakeProductDao implements ProductDao {

    private static final Map<Long, Product> STORES = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Product save(final Product entity) {
        Product product = generateProduct(++id, entity);
        STORES.put(id, product);
        return product;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.of(STORES.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(STORES.values());
    }

    public static void deleteAll() {
        STORES.clear();
    }
}
