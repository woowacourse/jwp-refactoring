package kitchenpos.product.application;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.annotation.ServiceTest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.presentation.dto.CreateProductRequest;
import kitchenpos.support.TestSupporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TestSupporter testSupporter;

    @Test
    void 상품을_생성한다() {
        // given
        final CreateProductRequest request = new CreateProductRequest("name", 123);

        // when
        final Product product = productService.create(request);

        // then
        assertAll(() -> assertThat(product.getName()).isEqualTo(request.getName()),
                  () -> assertThat(product.getPrice().getValue().doubleValue()).isEqualTo(request.getPrice()));

    }

    @ParameterizedTest(name = "가격이 {0}일때, 예외")
    @ValueSource(ints = {-1, -2})
    void 상품을_생성할_때_가격이_음수라면_예외가_발생한다(final long price) {
        // given
        final CreateProductRequest request = new CreateProductRequest("name", price);

        // when & then
        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품에_대해_전체_조회한다() {
        // given
        final Product product = testSupporter.createProduct();

        // when
        final List<Product> products = productService.list();

        // then
        assertThat(products.get(0)).isEqualTo(product);
    }
}