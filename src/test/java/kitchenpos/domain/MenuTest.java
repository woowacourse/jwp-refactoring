package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu.Builder;
import kitchenpos.exception.ExceptionType;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("Menu 객체 정상 생성")
    void create_success() {
        // given
        String name = "후라이드";
        MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity(null,
            ProductFixture.FRIED_CHICKEN.toEntity());
        BigDecimal price = BigDecimal.valueOf(menuProduct.getProduct().getPrice().longValue() * menuProduct.getQuantity());
        MenuGroup menuGroup = MenuGroupFixture.LUNCH.toEntity();

        // when
        Builder builder = new Builder()
            .name(name)
            .price(price)
            .menuGroup(menuGroup)
            .menuProducts(List.of(menuProduct));

        // then
        assertDoesNotThrow(builder::build);
    }

    @Test
    @DisplayName("Menu 객체 생성 실패 - 가격 합계가 메뉴 가격보다 큰 경우")
    void create_fail1() {
        // given
        String name = "후라이드";
        MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity(null,
            ProductFixture.FRIED_CHICKEN.toEntity());
        BigDecimal price = BigDecimal.valueOf(menuProduct.getProduct().getPrice().longValue() * menuProduct.getQuantity() + 1);
        MenuGroup menuGroup = MenuGroupFixture.LUNCH.toEntity();

        // when
        Builder builder = new Builder()
            .name(name)
            .price(price)
            .menuGroup(menuGroup)
            .menuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> builder.build()).hasMessageContaining(ExceptionType.MENU_PRICE_OVER_SUM.getMessage());
    }

    @Test
    @DisplayName("Menu 객체 생성 실패 - 가격이 0보다 작은 경우")
    void create_fail2() {
        // given
        String name = "후라이드";
        MenuProduct menuProduct = MenuProductFixture.FRIED_CHICKEN_MENU_PRODUCT.toEntity(null,
            ProductFixture.FRIED_CHICKEN.toEntity());
        BigDecimal price = BigDecimal.valueOf(-1);
        MenuGroup menuGroup = MenuGroupFixture.LUNCH.toEntity();

        // when
        Builder builder = new Builder()
            .name(name)
            .price(price)
            .menuGroup(menuGroup)
            .menuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> builder.build()).hasMessageContaining(ExceptionType.PRICE_RANGE.getMessage());
    }
}
