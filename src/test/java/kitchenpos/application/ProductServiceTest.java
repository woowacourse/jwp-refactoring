package kitchenpos.application;

import static kitchenpos.DomainFixture.뿌링클_생성;
import static kitchenpos.DomainFixture.치즈볼_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Nested
    class CreateTest {

        @Test
        void 상품을_생성하고_결과를_반환한다() {
            // when
            final var createdProduct = productService.create(뿌링클_생성());

            // then
            assertAll(
                    () -> assertThat(createdProduct.getId()).isNotNull(),
                    () -> assertThat(createdProduct.getName()).isEqualTo(뿌링클_생성().getName()),
                    () -> assertThat(createdProduct.getPrice()).isEqualByComparingTo(뿌링클_생성().getPrice())
            );
        }

        @Test
        void 상품_가격이_없는_경우_예외를_던진다() {
            // given
            final var product = 뿌링클_생성();
            product.setPrice(null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격이_0보다_작은_경우_예외를_던진다() {
            // given
            final var product = 뿌링클_생성();
            product.setPrice(BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품_가격은_0일_수_있다() {
            // given
            뿌링클_생성().setPrice(BigDecimal.valueOf(0));

            // when
            final var createdProduct = productService.create(뿌링클_생성());

            // then
            assertAll(
                    () -> assertThat(createdProduct.getId()).isNotNull(),
                    () -> assertThat(createdProduct.getName()).isEqualTo(뿌링클_생성().getName()),
                    () -> assertThat(createdProduct.getPrice()).isEqualByComparingTo(뿌링클_생성().getPrice())
            );
        }
    }

    @DisplayName("상품 목록 조회 테스트")
    @Nested
    class ListTest {

        @Test
        void 상품_목록을_조회한다() {
            // given
            productService.create(뿌링클_생성());
            productService.create(치즈볼_생성());

            // when
            final var foundProducts = productService.list();

            // then
            assertThat(foundProducts).hasSizeGreaterThanOrEqualTo(2);
        }
    }
}
