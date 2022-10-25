package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.ProductDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.fixture.ProductFixture;

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
        final Product product = ProductFixture.후라이드_치킨()
            .가격(new BigDecimal(15_000))
            .build();

        final Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("상품 등록 시 상품의 가격은 null 이 아니어야 한다")
    @Test
    void createPriceIsNull() {
        final Product product = ProductFixture.후라이드_치킨()
            .가격(null)
            .build();

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 시 상품의 가격은 0원 이상이어야 한다")
    @Test
    void createPriceIsLowerZero() {
        final Product product = ProductFixture.후라이드_치킨()
            .가격(new BigDecimal(-1L))
            .build();

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfProduct = 5;
        for (int i = 0; i < numberOfProduct; i++) {
            productDao.save(ProductFixture.후라이드_치킨().build());
        }

        final List<Product> products = productService.list();

        assertThat(products).hasSize(numberOfProduct);
    }
}
