package kitchenpos.application;

import static kitchenpos.fixture.Fixture.상품_강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록할_수_있다() {
        // given
        Product product = 상품_강정치킨();

        // when
        Product actual = productService.create(product);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @Test
    void 상품_목록들을_조회한다() {
        Product product = 상품_강정치킨();

        // when
        productService.create(product);

        List<Product> actual = productService.list();

        // then
        assertThat(actual).hasSize(1);
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
}
