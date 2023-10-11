package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    @Nested
    class 상품_생성_테스트 {

        @Test
        void 상품을_정상_생성한다() {
            // given
            Product product = new Product();
            product.setPrice(new BigDecimal(10_000));

            given(productDao.save(any(Product.class))).willReturn(new Product());

            // when, then
            assertDoesNotThrow(() -> productService.create(product));
        }

        @Test
        void 가격이_없는_상품을_생성_시_예외를_반환한다() {
            // given
            Product product = new Product();
            product.setPrice(null);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productService.create(product));
        }

        @Test
        void 음의_가격을_갖는_상품을_생성_시_예외를_반환한다() {
            // given
            Product product = new Product();
            product.setPrice(new BigDecimal(-1));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> productService.create(product));
        }
    }


    @Test
    void 상품을_전체_조회한다() {
        // given
        given(productDao.findAll()).willReturn(new ArrayList<>());

        // when, then
        assertThat(productService.list()).isInstanceOf(List.class);
    }
}
