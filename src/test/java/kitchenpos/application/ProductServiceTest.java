package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.CreateProductDto;
import kitchenpos.application.dto.ReadProductDto;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.exception.InvalidPriceException;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.ui.dto.request.CreateProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    void create_메서드는_product를_전달하면_product를_저장하고_반환한다() {
        // given
        final CreateProductRequest request = new CreateProductRequest("상품", BigDecimal.TEN);

        // when
        final CreateProductDto actual = productService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }
    
    @Test
    void create_메서드는_price가_null인_product를_전달하면_예외가_발생한다() {
        // given
        final CreateProductRequest invalidRequest = new CreateProductRequest("상품", null);

        // when & then
        assertThatThrownBy(() -> productService.create(invalidRequest))
                .isInstanceOf(InvalidPriceException.class);
    }

    @ParameterizedTest(name = "price가 {0}원이면 예외가 발생한다.")
    @ValueSource(longs = {-1L, -2L, -3L})
    void create_메서드는_price가_음수인_product를_전달하면_예외가_발생한다(final long invalidPrice) {
        // given
        final CreateProductRequest invalidRequest = new CreateProductRequest("상품", BigDecimal.valueOf(invalidPrice));

        // when & then
        assertThatThrownBy(() -> productService.create(invalidRequest))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    void list_메서드는_등록한_모든_product를_반환한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final Product expected = productRepository.save(product);

        // when
        final List<ReadProductDto> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expected.getId())
        );
    }
}
