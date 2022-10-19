package kitchenpos.application;

import static kitchenpos.KitchenPosFixtures.까르보치킨;
import static kitchenpos.KitchenPosFixtures.짜장치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("프로덕트를 생성할 수 있다")
    @Test
    void create() {
        // given
        final var productRequest = new Product("까르보치킨", new BigDecimal(20000));

        // when
        final var actual = productService.create(productRequest);

        // then
        assertAll(
                () -> assertThat(productRequest.getId()).isNull(),
                () -> assertThat(actual.getId()).isOne(),
                () -> assertThat(actual.getName()).isEqualTo(productRequest.getName()),
                () -> assertThat(actual.getPrice().doubleValue()).isEqualTo(productRequest.getPrice().doubleValue())
        );
    }

    @MethodSource("createProductFailCases")
    @ParameterizedTest(name = "{0}")
    void create_fail_cases(final String description, final String name, final BigDecimal price) {
        // given
        final var productRequest = new Product(name, price);

        // when & then
        assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(productRequest)
        );
    }

    private static Stream<Arguments> createProductFailCases() {
        return Stream.of(
                Arguments.of("Product 생성 시, name이 null이면 예외가 발생한다", null, new BigDecimal(20000)),
                Arguments.of("Product 생성 시, name이 비어있으면 예외가 발생한다", " ", new BigDecimal(20000)),
                Arguments.of("Product 생성 시, name이 비어있으면 예외가 발생한다", "", new BigDecimal(20000)),
                Arguments.of("Product 생성 시, price가 null이면 예외가 발생한다", "상품명", null),
                Arguments.of("Product 생성 시, price가 0 보다 작으면 예외가 발생한다", "상품명", new BigDecimal(-10000))
        );
    }

    @DisplayName("전체 프로덕트를 조회할 수 있다")
    @Test
    void list() {
        // given
        productService.create(까르보치킨);
        productService.create(짜장치킨);

        // when
        final var products = productService.list();
        final var prices = products.stream()
                .map(product -> product.getPrice().doubleValue())
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(products.size()).isEqualTo(2),
                () -> assertThat(products).extracting("id").containsExactly(1L, 2L),
                () -> assertThat(products).extracting("name").containsExactly(까르보치킨.getName(), 짜장치킨.getName()),
                () -> assertThat(prices).containsExactly(까르보치킨.getPrice().doubleValue(), 짜장치킨.getPrice().doubleValue())
        );
    }
}
