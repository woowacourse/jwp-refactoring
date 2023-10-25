package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;

public class InMemoryProductDao implements ProductDao {

    private final List<Product> products = new ArrayList<>();

    @Override
    public Product save(final Product entity) {
        final var id = (products.size() + 1);
        final var saved = new Product((long) id, entity.getName(), entity.getPrice());
        products.add(saved);
        return saved;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return products.stream()
                       .filter(product -> product.getId().equals(id))
                       .findAny();
    }

    @Override
    public List<Product> findAll() {
        return products;
    }
}
