package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product.Product;

public class InMemoryProductDao implements ProductDao {

    private final List<Product> products = new ArrayList<>();

    @Override
    public Product save(final Product entity) {
        entity.setId((long) (products.size() + 1));
        products.add(entity);
        return entity;
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
