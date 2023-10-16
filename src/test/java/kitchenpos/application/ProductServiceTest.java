package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @Nested
    class 상품_생성 {

        @Test
        void 가격은_null일_수_없다() {
            // given
            Product product = ProductFixture.fixture().price(null).build();

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격은_음수일_수_없다() {
            // given
            int price = -1000;
            Product product = ProductFixture.fixture().price(price).build();

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이름과_가격을_받아_상품을_생성한다() {
            // given
            int price = 1000;
            String name = "피자";
            Product product = ProductFixture.fixture()
                .name(name)
                .price(price)
                .build();

            Product expected = ProductFixture.fixture()
                .id(1L)
                .name(name)
                .price(price)
                .build();

            given(productDao.save(product))
                .willReturn(expected);

            // when
            Product actual = productService.create(product);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class 상품_전체_조회 {

        @Test
        void 전체_상품을_조회한다() {
            // given
            List<Product> expected = List.of(
                ProductFixture.fixture().id(1L).build(),
                ProductFixture.fixture().id(2L).build()
            );

            given(productDao.findAll())
                .willReturn(expected);

            // when
            List<Product> actual = productService.list();

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
