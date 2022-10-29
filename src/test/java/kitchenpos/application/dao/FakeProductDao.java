package kitchenpos.application.dao;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.fixture.ProductFixture.newProduct;

public class FakeProductDao implements ProductDao {

    private final Map<Long, Product> products = new HashMap<>();

    private long id = 1;

    @Override
    public Product save(final Product product) {
        final var newProduct = newProduct(
                id++,
                product.getName(),
                product.getPrice()
        );
        products.put(id++, newProduct);
        return product;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return products.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        products.clear();
    }
}
