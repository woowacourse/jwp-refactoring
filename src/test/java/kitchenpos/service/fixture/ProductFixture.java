package kitchenpos.service.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.service.dao.TestProductDao;

public class ProductFixture {

    public static final Long 후라이드 = 1L;
    public static final Long 양념치킨 = 2L;
    public static final Long 반반치킨 = 3L;
    public static final Long 통구이 = 4L;
    public static final Long 간장치킨 = 5L;
    public static final Long 순살치킨 = 6L;

    private final TestProductDao testProductDao;

    private ProductFixture(TestProductDao testProductDao) {
        this.testProductDao = testProductDao;
    }

    public static ProductFixture createFixture(){
        ProductFixture productFixture = new ProductFixture(new TestProductDao());
        productFixture.createProduct();
        return productFixture;
    }

    private void createProduct() {
        saveProduct("후라이드", new BigDecimal(16000));
        saveProduct("양념치킨", new BigDecimal(16000));
        saveProduct("반반치킨", new BigDecimal(16000));
        saveProduct("통구이", new BigDecimal(16000));
        saveProduct("간장치킨", new BigDecimal(17000));
        saveProduct("순살치킨", new BigDecimal(17000));
    }

    private Product saveProduct(String productName, BigDecimal price) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(price);
        return testProductDao.save(product);
    }

    public TestProductDao getTestProductDao(){
        return testProductDao;
    }
}
