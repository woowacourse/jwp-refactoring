package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productDao = ProductFixture.setUp().getProductDao();
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        final String productName = "맥북m1";
        final BigDecimal productPrice = BigDecimal.valueOf(3000);
        final ProductCreateRequest request = ProductFixture.createProductRequest(productName, productPrice);

        final ProductResponse response = productService.create(request);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo(productName),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(productPrice)
        );
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void getProducts() {
        final List<ProductResponse> expectedProducts = ProductFixture.setUp()
                .getFixtures();
        final List<ProductResponse> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSameSizeAs(expectedProducts),
                () -> assertThat(products).usingRecursiveComparison()
                        .isEqualTo(expectedProducts)
        );
    }
}
