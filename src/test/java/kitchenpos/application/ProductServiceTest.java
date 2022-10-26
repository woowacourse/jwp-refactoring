package kitchenpos.application;

import static kitchenpos.Fixture.상품의_가격은;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        Product actual = productService.create(상품의_가격은(new BigDecimal(10_000)));
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        Product product = 상품의_가격은(null);

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        Product product = 상품의_가격은(new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품을_조회한다() {
        상품_생성(10_000);

        List<Product> products = productService.list();

        assertThat(products).hasSizeGreaterThanOrEqualTo(1);
    }
}
