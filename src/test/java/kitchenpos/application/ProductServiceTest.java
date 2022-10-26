package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        // given
        Product product = 강정치킨();

        // when
        Product actual = productService.create(product);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(product.getName()),
                () -> assertThat(actual.getPrice()).isCloseTo(product.getPrice(), Percentage.withPercentage(0))
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -1000})
    void 상품_등록_시_상품_가격이_0_이하라면_예외를_던진다(int price) {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(price));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_등록_시_상품_가격이_null이라면_예외를_던진다() {
        // given
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(null);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록들을_조회한다() {
        // given
        Product product = 강정치킨();
        productService.create(product);

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    public Product 강정치킨() {
        Product product = new Product();
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));

        return product;
    }
}
