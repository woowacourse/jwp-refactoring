package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @DisplayName("객체 생성: 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        final Long id = 1L;
        final String name = "바삭치킨 두마리";
        final BigDecimal price = new BigDecimal(24000);
        final MenuGroup menuGroup = new MenuGroup(1L, "메인메뉴");
        final List<MenuProduct> menuProducts = new ArrayList<>();

        // when
        Menu menu = new Menu(id, name, price, menuGroup, menuProducts);

        // then
        assertThat(menu.getId()).isEqualTo(id);
        assertThat(menu.getName()).isEqualTo(name);
        assertThat(menu.getPrice()).isEqualTo(price);
        assertThat(menu.getMenuGroup()).isEqualTo(menuGroup);
        assertThat(menu.getMenuProducts()).isEmpty();
    }

    @DisplayName("객체 생성: 객체가 생성시 가격이 음수면 예외가 발생한다.")
    @Test
    void createWithNegativePrice() {
        // given
        final Long id = 1L;
        final String name = "바삭치킨 두마리";
        final BigDecimal price = new BigDecimal(-1);
        final MenuGroup menuGroup = new MenuGroup(1L, "메인메뉴");
        final List<MenuProduct> menuProducts = new ArrayList<>();

        // when then
        assertThatThrownBy(() -> new Menu(id, name, price, menuGroup, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("검증: 메뉴의 가격은 상품들의 가격(상품의 가격 * 개수)총합보다 작거나 같아야한다. [메뉴의 가격 <= 상품들 전체의 가격]")
    @Test
    void validatePrice() {
        // given
        final Long id = 1L;
        final String name = "바삭치킨 두마리";
        final BigDecimal price = new BigDecimal(24000);
        final MenuGroup menuGroup = new MenuGroup(1L, "메인메뉴");
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final Menu menu = new Menu(id, name, price, menuGroup, menuProducts);

        final BigDecimal productsTotalPrice1 = new BigDecimal(24000);
        final BigDecimal productsTotalPrice2 = new BigDecimal(23999);

        // when then
        assertDoesNotThrow(() -> menu.validatePrice(productsTotalPrice1));
        assertThatThrownBy(() -> menu.validatePrice(productsTotalPrice2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Getter: 메뉴 그룹의 id 반환한다.")
    @Test
    void getMenuGroupId() {
        // given
        final Long id = 1L;
        final String name = "바삭치킨 두마리";
        final BigDecimal price = new BigDecimal(24000);
        final MenuGroup menuGroup = new MenuGroup(1L, "메인메뉴");
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final Menu menu = new Menu(id, name, price, menuGroup, menuProducts);

        // when
        Long expectedMenuGroupId = 1L;
        Long menuGroupId = menu.getMenuGroupId();

        // then
        assertThat(menuGroupId).isEqualTo(expectedMenuGroupId);
    }
}