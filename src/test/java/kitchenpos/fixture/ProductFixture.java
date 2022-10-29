package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.InMemoryProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final ProductDao productDao;
    private List<ProductResponse> fixtures;

    public ProductFixture(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public static ProductFixture setUp() {
        final ProductFixture productFixture = new ProductFixture(new InMemoryProductDao());
        productFixture.fixtures = productFixture.createProductsResponse();
        return productFixture;
    }

    public static ProductCreateRequest createProductRequest(final String productName, final BigDecimal productPrice) {
        return new ProductCreateRequest(productName, productPrice);
    }

    private List<ProductResponse> createProductsResponse() {
        return List.of(
                ProductResponse.from(saveProduct("맵슐랭", new BigDecimal(21000))),
                ProductResponse.from(saveProduct("허니콤보", new BigDecimal(20000)))
        );
    }

    private Product saveProduct(final String productName, final BigDecimal price) {
        return productDao.save(new Product(productName, price));
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public List<ProductResponse> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }
}
