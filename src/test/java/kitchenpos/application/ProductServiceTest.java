package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static kitchenpos.testutils.TestDomainBuilder.productBuilder;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@MockitoSettings
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // given
        Product newProduct = productBuilder()
                .name("공짜 상품")
                .price(BigDecimal.valueOf(0))
                .build();

        // when
        productService.create(newProduct);

        // then
        then(productDao)
                .should(times(1))
                .save(newProduct);
    }

    @DisplayName("상품 생성에 실패한다 - 가격은 null 이거나 음수일 수 없다.")
    @MethodSource("invalidPriceSource")
    @ParameterizedTest
    void createWithInvalidPrice(BigDecimal price) {
        // given
        Product newProduct = productBuilder()
                .name("유효하지 않은 가격의 상품")
                .price(price)
                .build();

        // when, then
        assertThatThrownBy(() -> productService.create(newProduct))
                .isInstanceOf(IllegalArgumentException.class);

        then(productDao).should(never()).save(any(Product.class));
    }

    private static Stream<BigDecimal> invalidPriceSource() {
        return Stream.of(
                null,
                BigDecimal.valueOf(-1),
                BigDecimal.valueOf(-10000)
        );
    }

    @DisplayName("전체 상품 리스트를 가져온다.")
    @Test
    void list() {
        // when
        productService.list();

        // then
        then(productDao)
                .should(times(1))
                .findAll();
    }
}

