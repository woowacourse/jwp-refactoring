package kitchenpos.application.dao;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeProductDao implements ProductDao {

    private final Map<Long, Product> products = new HashMap<>();

    private long id = 1;

    @Override
    public Product save(final Product product) {
        final var newProduct = makeNew(product);
        products.put(newProduct.getId(), newProduct);
        return newProduct;
    }

    private Product makeNew(final Product product) {
        return new Product(
                id++,
                product.getName(),
                product.getPrice()
        );
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
