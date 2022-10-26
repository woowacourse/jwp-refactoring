package kitchenpos.dao.fake;

import static kitchenpos.application.fixture.ProductFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class FakeProductDao implements ProductDao {

    private static final Map<Long, Product> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Product save(final Product entity) {
        Product product = generateProduct(++id, entity);
        stores.put(id, product);
        return product;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(stores.values());
    }

    public static void deleteAll() {
        stores.clear();
    }
}
