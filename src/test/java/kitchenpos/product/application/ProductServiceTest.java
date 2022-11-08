package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.common.fixture.Fixture;
import kitchenpos.product.application.request.ProductCreateRequest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 가격이_null인_경우 extends ServiceTest {
            private final ProductCreateRequest request = new ProductCreateRequest("상품", null);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
            }
        }

        @Nested
        class 가격이_0_미만일_경우 extends ServiceTest {

            private static final int INVALID_PRICE = -1000;

            private final ProductCreateRequest request = new ProductCreateRequest("상품", new BigDecimal(INVALID_PRICE));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> productService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("가격은 null 혹은 0 미만일 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_입력을_받을_경우 extends ServiceTest {

            private final ProductCreateRequest request = new ProductCreateRequest("상품", new BigDecimal(10000));

            @Test
            void Product를_생성하고_반환한다() {
                final Product actual = productService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getName()).isEqualTo("상품"),
                        () -> assertThat(actual.getProductPrice().getValue()).isEqualByComparingTo(
                                new BigDecimal(10000))
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출하는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 2;

            @Test
            void Product의_목록을_반환한다() {
                productService.create(Fixture.후라이드상품_생성요청);
                productService.create(Fixture.양념치킨상품_생성요청);

                final List<Product> actual = productService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }
}
