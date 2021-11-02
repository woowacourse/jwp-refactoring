package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dao.TestProductDao;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long 후라이드 = 1L;
    public static final Long 양념치킨 = 2L;
    public static final Long 반반치킨 = 3L;
    public static final Long 통구이 = 4L;
    public static final Long 간장치킨 = 5L;
    public static final Long 순살치킨 = 6L;

    private List<Product> fixtures;

    private final TestProductDao testProductDao;

    private ProductFixture(TestProductDao testProductDao) {
        this.testProductDao = testProductDao;
    }

    public static ProductFixture createFixture() {
        ProductFixture productFixture = new ProductFixture(new TestProductDao());
        productFixture.fixtures = productFixture.createProduct();
        return productFixture;
    }

    private List<Product> createProduct() {
        return Arrays.asList(
            saveProduct("후라이드", new BigDecimal(16000)),
            saveProduct("양념치킨", new BigDecimal(16000)),
            saveProduct("반반치킨", new BigDecimal(16000)),
            saveProduct("통구이", new BigDecimal(16000)),
            saveProduct("간장치킨", new BigDecimal(17000)),
            saveProduct("순살치킨", new BigDecimal(17000))
        );
    }

    private Product saveProduct(String productName, BigDecimal price) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        return testProductDao.save(product);
    }

    public TestProductDao getTestProductDao() {
        return testProductDao;
    }

    public List<Product> getFixtures() {
        return fixtures;
    }
}
