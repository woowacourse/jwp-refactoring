package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class InmemoryProductDao implements ProductDao {

    private final Map<Long, Product> orderTables;
    private long idValue;

    public InmemoryProductDao() {
        idValue = 0;
        orderTables = new LinkedHashMap<>();
    }

    @Override
    public Product save(Product entity) {
        long savedId = idValue;
        Product product = new Product();
        product.setId(savedId);
        product.setName(entity.getName());
        product.setPrice(entity.getPrice());
        this.orderTables.put(savedId, product);
        idValue++;
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(orderTables.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(orderTables.values());
    }

}
