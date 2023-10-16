package kitchenpos.legacy.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.legacy.dao.ProductDao;
import kitchenpos.legacy.domain.Product;
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
class LegacyProductServiceTest {

    @InjectMocks
    LegacyProductService productService;

    @Mock
    ProductDao productDao;

    @Nested
    class create {

        @Test
        void 가격이_null이면_예외() {
            // given
            Product product = new Product();
            product.setPrice(null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_0보다_작으면_에외() {
            // given
            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_0원이어도_성공() {
            // given
            Product product = new Product();
            product.setPrice(BigDecimal.ZERO);
            Product savedProduct = new Product();
            savedProduct.setId(1L);
            given(productDao.save(any(Product.class)))
                .willReturn(savedProduct);

            // when & then
            assertThatNoException()
                .isThrownBy(() -> productService.create(product));
        }

        @Test
        void 성공() {
            // given
            Product product = new Product();
            product.setPrice(BigDecimal.valueOf(1000));
            Product savedProduct = new Product();
            savedProduct.setId(1L);
            given(productDao.save(any(Product.class)))
                .willReturn(savedProduct);

            // when
            Product actual = productService.create(product);

            // then
            assertThat(actual.getId()).isEqualTo(1L);
        }
    }

    @Nested
    class findAll {

        @Test
        void 성공() {
            // given
            given(productDao.findAll())
                .willReturn(List.of(new Product(), new Product()));

            // when
            List<Product> actual = productService.findAll();

            // when & then
            assertThat(actual).hasSize(2);
        }
    }
}
