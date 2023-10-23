package kitchenpos.domain.menu;

import static kitchenpos.common.fixtures.MenuFixtures.MENU1_NAME;
import static kitchenpos.common.fixtures.MenuGroupFixtures.MENU_GROUP1;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT1;
import static kitchenpos.common.fixtures.MenuProductFixtures.MENU_PRODUCT2;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {


    @Test
    @DisplayName("생성 시 메뉴 가격이 메뉴 상품 가격 합보다 크면 예외가 발생한다.")
    void throws_menuPriceLargeThanSum() {
        // given
        final MenuProduct menuProduct1 = MENU_PRODUCT1();

        final BigDecimal price1 = menuProduct1.getProduct().calculateTotalPrice(menuProduct1.getQuantity());
        final BigDecimal price2 = menuProduct1.getProduct().calculateTotalPrice(menuProduct1.getQuantity());
        final BigDecimal overedTotalPrice = price1.add(price2).add(BigDecimal.ONE);

        // when & then
        assertThatThrownBy(() -> new Menu(MENU1_NAME, overedTotalPrice, MENU_GROUP1(), List.of(MENU_PRODUCT1(), MENU_PRODUCT2())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 메뉴의 가격이 메뉴 상품 가격의 합보다 클 수 없습니다.");
    }
}
