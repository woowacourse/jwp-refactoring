package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class InMemoryProductDao implements ProductDao {
    private Map<Long, Product> products;
    private long index;

    public InMemoryProductDao() {
        this.products = new HashMap<>();
        this.index = 0;
    }

    @Override
    public Product save(final Product entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final Product product = new Product();
        product.setId(key);
        product.setName(entity.getName());
        product.setPrice(entity.getPrice());

        products.put(key, product);
        return product;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.of(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
}
