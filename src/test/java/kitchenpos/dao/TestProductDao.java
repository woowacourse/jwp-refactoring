package kitchenpos.dao;

import java.util.Comparator;
import java.util.function.BiConsumer;
import kitchenpos.domain.Product;

public class TestProductDao extends TestAbstractDao<Product> implements ProductDao {

    @Override
    protected BiConsumer<Product, Long> setIdConsumer() {
        return Product::setId;
    }

    @Override
    protected Comparator<Product> comparatorForSort() {
        return Comparator.comparingLong(Product::getId);
    }
}
