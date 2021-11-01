package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.factory.ProductFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void list() {
        // when
        productService.list();

        // then
        verify(productDao, times(1)).findAll();
    }

    @Nested
    class CreateTest {

        private Product product;

        private Product savedProduct;

        @BeforeEach
        void setUp() {
            product = ProductFactory.builder()
                .price(new BigDecimal(17000))
                .build();
            savedProduct = ProductFactory.copy(product)
                .id(1L)
                .build();

        }

        @DisplayName("Product 를 생성한다")
        @Test
        void create() {
            // given
            given(productDao.save(product)).willReturn(savedProduct);

            // when
            Product result = productService.create(product);

            // then
            assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedProduct);
        }

        @DisplayName("Product 생성 실패한다 - product 의 price 가 null 인 경우")
        @Test
        void create_whenPriceIsNull() {
            // given
            product = ProductFactory.copy(product)
                .price(null)
                .build();

            // when
            ThrowingCallable throwingCallable = () -> productService.create(product);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Product 생성 실패한다 - product 의 price 가 음수인 경우")
        @Test
        void create_whenPriceIsNegative() {
            // given
            product = ProductFactory.copy(product)
                .price(new BigDecimal(-1))
                .build();

            // when
            ThrowingCallable throwingCallable = () -> productService.create(product);

            // then
            assertThatThrownBy(throwingCallable)
                .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}
