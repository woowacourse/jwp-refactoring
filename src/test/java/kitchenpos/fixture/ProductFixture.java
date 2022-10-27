package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.InMemoryProductDao;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final InMemoryProductDao inMemoryProductDao;
    private List<Product> fixtures;

    public ProductFixture(final InMemoryProductDao inMemoryProductDao) {
        this.inMemoryProductDao = inMemoryProductDao;
    }

    public static ProductFixture setUp() {
        final ProductFixture productFixture = new ProductFixture(new InMemoryProductDao());
        productFixture.fixtures = productFixture.createProducts();
        return productFixture;
    }

    public static Product createProductByPrice(final BigDecimal price) {
        final Product product = new Product();
        product.setName("맥북m1");
        product.setPrice(price);
        return product;
    }

    public static Product createProduct(final String productName, final BigDecimal productPrice) {
        final Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);
        return product;
    }

    private List<Product> createProducts() {
        return List.of(
                saveProduct("맵슐랭", new BigDecimal(21000)),
                saveProduct("허니콤보", new BigDecimal(20000))
        );
    }

    private Product saveProduct(final String productName, final BigDecimal price) {
        final Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        return inMemoryProductDao.save(product);
    }

    public InMemoryProductDao getInMemoryProductDao() {
        return inMemoryProductDao;
    }

    public List<Product> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }
}
