package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Nested
    class 생성_테스트 {

        @Test
        void 상품의_0_보다_작으면_예외() {
            // given
            Product product = ProductFixture.builder()
                .withPrice(-1L)
                .build();

            // when && then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품의_가격을_명시하지않으면_예외() {
            // given
            Product product = ProductFixture.builder()
                .build();

            // when && then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_성공() {
            // given
            Product product = ProductFixture.builder()
                .withPrice(1000L)
                .build();
            given(productDao.save(any()))
                .willReturn(product);

            // when
            Product actual = productService.create(product);

            // then
            assertThat(actual).isEqualTo(product);
        }
    }

    @Test
    void 목록_조회() {
        // given
        List<Product> products = List.of(ProductFixture.builder().build());
        given((productDao.findAll()))
            .willReturn(products);

        // when
        List<Product> actual = productService.list();

        // then
        assertThat(actual).isEqualTo(products);
    }
}
