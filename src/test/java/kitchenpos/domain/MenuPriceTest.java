package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격은")
class MenuPriceTest {

    @Test
    @DisplayName("0원 미만일 경우 예외가 발생한다.")
    void priceLessThanZero() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MenuPrice.from(-1))
                .withMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("요청한 가격보다 크면 true를 반환한다.")
    void isGreaterThan() {
        MenuPrice menuPrice = MenuPrice.from(18000);

        assertThat(menuPrice.isGreaterThan(ProductPrice.from(1900))).isTrue();
    }
}
