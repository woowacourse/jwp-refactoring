package kitchenpos.domain;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    private static final long ID = 1L;
    private static final MenuGroup MENU_GROUP = MenuGroupFixture.create();
    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);

    @DisplayName("메뉴 추가 - 실패 - 유효한 가격이 아닌 경우")
    @Test
    void createFailureWhenInvalidPriceOrNullPrice() {
        //given
        //when
        //then
        assertThatThrownBy(() -> MenuFixture.create(ID, "INVALID", INVALID_PRICE, MENU_GROUP))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> MenuFixture.create(ID, "INVALID", null, MENU_GROUP))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu의 가격이 MenuProducts 가격의 합을 넘는 경우 예외를 던진다.")
    @Test
    void createFailureWhenInvalidMenuGroupPrice() {
        Menu menu = MenuFixture.create();
        MenuProduct menuProduct = new MenuProduct(menu, ProductFixture.create(), 1L);

        BigDecimal productPrice = menuProduct.getProduct().getPrice();
        BigDecimal productQuantity = BigDecimal.valueOf(menuProduct.getQuantity());

        BigDecimal totalPrice = productPrice.multiply(productQuantity);

        assertThatThrownBy(() -> menu.validateTotalPrice(totalPrice.min(BigDecimal.ONE)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
