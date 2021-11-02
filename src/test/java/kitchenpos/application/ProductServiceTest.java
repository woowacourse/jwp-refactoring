package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataIntegrityViolationException;

class ProductServiceTest extends IntegrationTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        Product 민성치킨 = TProduct.builder()
            .name("민성치킨")
            .price(BigDecimal.valueOf(1000000000))
            .build();
        Product product = productService.create(민성치킨);

        assertAll(
            () -> assertThat(product)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id", ".*price")
                .isEqualTo(민성치킨),
            () -> assertThat(product.getPrice().intValue()).isEqualTo(민성치킨.getPrice().intValue())
        );
    }

    @DisplayName("상품의 이름은 null일 수 없다.")
    @Test
    void create_nameCanNotBeNull() {
        Product 널치킨 = TProduct.builder()
            .price(BigDecimal.valueOf(1000000000))
            .build();

        assertThatThrownBy(() -> productService.create(널치킨))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("상품의 가격은 null이거나 음수일 수 없다.")
    @ParameterizedTest
    @MethodSource("getParametersForPriceCannotBeNullOrNegative")
    void create_priceCannotBeNullOrNegative(BigDecimal price) {
        Product 널치킨 = TProduct.builder()
            .price(price)
            .build();

        assertThatThrownBy(() -> productService.create(널치킨))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getParametersForPriceCannotBeNullOrNegative() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(BigDecimal.valueOf(-1))
        );
    }

    @DisplayName("상품 목록을 가져온다.")
    @Test
    void list() {
        productService.create(TProduct.강정치킨());
        productService.create(TProduct.후라이드());

        List<Product> list = productService.list();
        assertThat(list).hasSize(2);
    }
}
