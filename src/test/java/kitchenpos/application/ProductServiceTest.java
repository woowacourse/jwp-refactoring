package kitchenpos.application;

import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_가격;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    private Product 야채곱창;

    @BeforeEach
    void setUp() {
        야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(야채곱창_가격)
                .build();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void 상품을_등록한다() {
        // when
        Product actual = productService.create(야채곱창);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(야채곱창_이름),
                () -> assertThat(actual.getPrice()).isEqualTo(야채곱창_가격)
        );
    }

    @DisplayName("상품을 등록할 때, 가격이 0원 보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -100})
    void 상품을_등록할_때_가격이_0원_보다_작으면_예외가_발생한다(int 잘못된_가격) {
        // given
        Product 야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(BigDecimal.valueOf(잘못된_가격))
                .build();

        // when & then
        assertThatThrownBy(() -> productService.create(야채곱창))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 등록할 때, 가격이 null 이면 예외가 발생한다.")
    @Test
    void 상품을_등록할_때_가격이_null_이면_예외가_발생한다() {
        // given
        Product 야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(null)
                .build();

        // when & then
        assertThatThrownBy(() -> productService.create(야채곱창))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록을_조회한다() {
        // given
        productService.create(야채곱창);

        // when
        List<Product> 상품들 = productService.list();

        // then
        assertThat(상품들).extracting(Product::getName)
                .contains(야채곱창_이름);
    }
}