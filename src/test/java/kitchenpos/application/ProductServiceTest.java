package kitchenpos.application;

import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.ProductRepository;
import kitchenpos.domain.menu.service.ProductService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static kitchenpos.application.fixture.ProductFixture.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.only;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    class Create {

        @Test
        void 상품을_생성할_수_있다() {
            // given
            final Product expected = product("치킨", BigDecimal.valueOf(20_000));
            given(productRepository.save(expected)).willReturn(expected);

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
            final Product expected = product("치킨", BigDecimal.valueOf(-1));

            // when, then
            assertThatThrownBy(() -> productService.create(expected))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 상품을_전체_조회할_수_있다() {
            // when
            productService.list();

            // then
            verify(productRepository, only()).findAll();
        }
    }
}
