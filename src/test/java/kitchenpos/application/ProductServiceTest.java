package kitchenpos.application;

import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeProductDao;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Product 서비스 테스트")
class ProductServiceTest {

    private ProductService productService;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new FakeProductDao();
        productService = new ProductService(productDao);
    }

    @DisplayName("상품을 등록한다")
    @Test
    void create() {
        final ProductRequest request = new ProductRequest("후라이드", BigDecimal.valueOf(15_000));

        final ProductResponse response = productService.create(request);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("상품의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfProduct = 5;
        for (int i = 0; i < numberOfProduct; i++) {
            productDao.save(후라이드_치킨());
        }

        final List<ProductResponse> responses = productService.list();

        assertThat(responses).hasSize(numberOfProduct);
    }
}
