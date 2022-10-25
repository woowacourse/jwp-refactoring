package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidProductPriceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    @Nested
    class 상품_생성 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // given
            Product product = new Product("치킨", BigDecimal.valueOf(1000));

            // when
            Product extract = productService.create(product);

            // then
            assertThat(extract).isNotNull();
        }

        @Test
        void 요청에서_가격이_없을_경우_예외가_발생한다() {
            // given
            Product product = new Product("짜장면", BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(InvalidProductPriceException.class);
        }

        @Test
        void 요청에서_가격이_NULL_일_경우_예외가_발생한다() {
            // given
            Product product = new Product("짜장면", null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(InvalidProductPriceException.class);
        }
    }

    @Nested
    class 상품_리스트_조회 extends IntegrationTest {
        @Test
        void 요청을_할_수_있다() {
            // when
            List<Product> extract = productService.list();

            // then
            assertThat(extract).hasSize(6);
        }
    }
}