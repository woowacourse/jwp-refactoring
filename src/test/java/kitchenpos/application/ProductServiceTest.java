package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest extends ServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 상품을_생성할_수_있다() {
            // given
            final var product = new Product("상품_이름", BigDecimal.valueOf(1000));

            given(productDao.save(product)).willReturn(product);

            // when
            final var actual = productService.create(product);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(product);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 상품의_금액이_NULL이면_에러를_반환한다() {
            // given
            final var product = new Product("상품_이름", null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 상품의 가격이 없거나, 음수입니다.");
        }

        @Test
        void 상품의_금액이_음수이면_에러를_반환한다() {
            // given
            final var product = new Product("상품_이름", BigDecimal.valueOf(-1000));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 상품의 가격이 없거나, 음수입니다.");
        }
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 주문_목록이_존재하지_않으면_빈_값을_반환한다() {
            // given
            given(productService.list()).willReturn(Collections.emptyList());

            // when
            final var actual = productService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 주문이_하나_이상_존재하면_주문_목록을_반환한다() {
            // given
            final var product = new Product("상품_이름", BigDecimal.valueOf(1000));

            given(productService.list()).willReturn(List.of(product));

            final var expected = List.of(product);

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
