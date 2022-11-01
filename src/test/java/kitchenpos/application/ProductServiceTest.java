package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductResponse;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        Product productRequest = new Product("햄버거", new BigDecimal(10_000));

        ProductResponse actual = productService.create(productRequest);

        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> productService.create(new Product("햄버거", null)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("price가 존재하지 않습니다.");
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> productService.create(new Product("햄버거", new BigDecimal(-1))))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("price가 음수입니다.");
    }

    @Test
    void 모든_상품을_조회한다() {
        상품_생성(10_000);

        List<ProductResponse> products = productService.list();

        assertThat(products).hasSizeGreaterThanOrEqualTo(1);
    }
}
