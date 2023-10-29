package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class create_성공_테스트 {

        @Test
        void 상품을_생성할_수_있다() {
            // given
            final var request = new ProductCreateRequest("상품_이름", BigDecimal.valueOf(1000));

            // when
            final var actual = productService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = productService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            final var product = productRepository.save(new Product("상품_이름", BigDecimal.valueOf(1000)));

            final var expected = List.of(ProductResponse.from(product));

            // when
            final var actual = productService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }
}
