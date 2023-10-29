package kitchenpos.core.domain.menu;

import static kitchenpos.core.fixture.ProductFixture.치킨_8000원;
import static kitchenpos.core.fixture.ProductFixture.피자_8000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.core.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    @DisplayName("메뉴 상품의 최소 개수는 1개이다.")
    void 메뉴_상품_생성_실패_최소_개수() {
        assertThatThrownBy(() -> new MenuProducts(Collections.emptyList()))
                .isInstanceOf(Exception.class);
    }

    @Nested
    class TotalPrice {
        @Test
        void 메뉴_상품_총합() {
            // given
            final MenuProducts menuProducts = new MenuProducts(List.of(
                    new MenuProduct(치킨_8000원(), 2L),
                    new MenuProduct(피자_8000원(), 1L)
            ));

            // expected
            assertThat(menuProducts.totalPrice())
                    .isEqualTo(Price.from(BigDecimal.valueOf(24000)));
        }
    }
}
