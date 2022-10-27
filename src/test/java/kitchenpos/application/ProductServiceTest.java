package kitchenpos.application;

import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨;
import static kitchenpos.domain.fixture.ProductFixture.후라이드_치킨의_가격은;
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
        final Product product = 후라이드_치킨();

        final Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("상품 등록 시 상품의 가격은 null 이 아니어야 한다")
    @Test
    void createPriceIsNull() {
        final Product product = 후라이드_치킨의_가격은(null);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 등록 시 상품의 가격은 0원 이상이어야 한다")
    @Test
    void createPriceIsLowerZero() {
        final Product product = 후라이드_치킨의_가격은(new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회한다")
    @Test
    void list() {
        final int numberOfProduct = 5;
        for (int i = 0; i < numberOfProduct; i++) {
            productDao.save(후라이드_치킨());
        }

        final List<Product> products = productService.list();

        assertThat(products).hasSize(numberOfProduct);
    }
}
