package kitchenpos.menu;

import static kitchenpos.common.fixture.MenuGroupFixtures.generateMenuGroup;
import static kitchenpos.common.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.common.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.UnitTest;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class MenuTest {

    @Test
    void menu를_생성한다() {
        MenuGroup 한마리메뉴 = generateMenuGroup("한마리메뉴");
        Product 후라이드 = generateProduct("후라이드");
        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));

        Menu actual = new Menu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts);

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo("후라이드치킨");
            assertThat(actual.getMenuGroupId()).isEqualTo(한마리메뉴.getId());
            assertThat(actual.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void menu를_생성할_때_price가_null인_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Menu("후라이드치킨", null, 1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void menu를_생성할_때_price가_0미만인_경우_예외를_던진다(final int price) {
        assertThatThrownBy(() -> new Menu("후라이드치킨", BigDecimal.valueOf(price), 1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu를_생성할_때_price가_menu에_속한_product의_총_price보다_큰_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = generateMenuGroup("한마리메뉴");
        Product 후라이드 = generateProduct("후라이드", BigDecimal.valueOf(16000));
        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));

        assertThatThrownBy(() -> new Menu("후라이드치킨", BigDecimal.valueOf(17000), 한마리메뉴.getId(), menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
