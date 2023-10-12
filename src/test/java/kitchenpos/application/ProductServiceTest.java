package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Nested
    class Create {

        @Test
        void 상품을_생성할_수_있다() {
            // given
            final Product expected = new Product("치킨", BigDecimal.valueOf(20_000));
            given(productDao.save(expected)).willReturn(expected);

            // when
            final Product actual = productService.create(expected);

            // then
            assertAll(
                    () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                    () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice())
            );
        }

        @Test
        void 상품의_가격이_null이면_상품을_생성할_수_없다() {
            // given
            final Product expected = new Product("치킨", null);

            // when, then
            assertThatThrownBy(() -> productService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품의_가격이_0보다_작으면_상품을_생성할_수_없다() {
            // given
            final Product expected = new Product("치킨", BigDecimal.valueOf(-1));

            // when, then
            assertThatThrownBy(() -> productService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 상품을_전체_조회할_수_있다() {
            // given
            final Product chicken = new Product("치킨", BigDecimal.valueOf(20_000));
            final Product pizza = new Product("피자", BigDecimal.valueOf(24_000));
            given(productDao.findAll()).willReturn(List.of(chicken, pizza));

            // when
            final List<Product> actual = productService.list();

            // then
            assertThat(actual).containsExactly(chicken, pizza);
        }
    }
}
