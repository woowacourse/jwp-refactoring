package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Products extends AbstractList<Product> {

    @MappedCollection(idColumn = "id")
    final List<Product> products;

    public Products(final List<Product> products) {
        this.products = products;
    }

    @Override
    public Product get(final int index) {
        return products.get(index);
    }

    @Override
    public int size() {
        return products.size();
    }

    public MenuPrice getTotalPrice() {
        return new MenuPrice(products.stream()
                                 .map(Product::getPrice)
                                 .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
