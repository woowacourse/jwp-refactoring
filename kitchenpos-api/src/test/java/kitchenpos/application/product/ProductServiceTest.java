package kitchenpos.application.product;

import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.support.ServiceTest;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given
        ProductRequest request = new ProductRequest("chicken", BigDecimal.valueOf(10000));

        // when
        Product savedProduct = productService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedProduct).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(new Product("chicken", BigDecimal.valueOf(10000)));
            softly.assertThat(savedProduct.getId()).isNotNull();
        });
    }

    @Test
    void 상품을_생성할_때_상품_가격이_음수면_예외가_발생한다() {
        // given
        ProductRequest invalidProduct = new ProductRequest("chicken", BigDecimal.valueOf(-1L));

        // expect
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0원 이상이여야합니다");
    }
}
