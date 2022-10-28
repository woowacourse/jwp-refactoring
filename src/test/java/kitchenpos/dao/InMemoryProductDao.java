package kitchenpos.dao;

import java.util.function.BiConsumer;
import kitchenpos.domain.Product;

public class InMemoryProductDao extends InMemoryAbstractDao<Product> implements ProductDao {

    @Override
    protected BiConsumer<Product, Long> setId() {
        return Product::setId;
    }
}
