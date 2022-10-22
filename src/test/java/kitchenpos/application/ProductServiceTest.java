package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("강정치킨", new BigDecimal(17000));
        product.setId(1L);
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        final Product requestProduct = new Product("강정치킨", new BigDecimal(17000));
        when(productDao.save(any())).thenReturn(product);

        // when
        final Product savedProduct = productService.create(requestProduct);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("강정치킨"),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal(17000))
        );
    }

    @Test
    @DisplayName("싱품의 가격이 0일 경우 상품이 생성되어야 한다.")
    void createWithValidPrice() {
        // given
        final Product requestProduct = new Product("강정치킨", new BigDecimal(0));
        product.setPrice(new BigDecimal(0));
        when(productDao.save(any())).thenReturn(product);

        // when
        final Product savedProduct = productService.create(requestProduct);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(new BigDecimal(0))
        );
    }

    @ParameterizedTest(name = "상품의 가격이 {1}일 경우 예외가 발생한다.")
    @MethodSource("invalidParams")
    void createWithInvalidPrice(final Product product, final String testName) {
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParams() {
        return Stream.of(
                Arguments.of(new Product("강정치킨", new BigDecimal(-100)), "-100"),
                Arguments.of(new Product("강정치킨", new BigDecimal(-1)), "-1"),
                Arguments.of(new Product("강정치킨", null), "null")
        );
    }
}
