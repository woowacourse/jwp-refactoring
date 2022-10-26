package kitchenpos.application;

import static kitchenpos.support.fixture.MenuFixture.createPepperoniMenu;
import static kitchenpos.support.fixture.MenuProductFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 등록 기능")
    @Nested
    class CreateTest {

        @DisplayName("정상 작동")
        @Test
        void create() {
            final Menu pepperoniMenu = createPepperoniMenu(menuGroup.getId(), menuProducts);

            final Menu savedMenu = menuService.create(pepperoniMenu);

            assertThat(savedMenu.getId()).isNotNull();
        }

        @DisplayName("메뉴의 가격이 null 이거나 음수이면 예외가 발생한다.")
        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"-1"})
        void create_Exception_Price(BigDecimal price) {
            final Menu menu = new Menu("두마리메뉴", price, menuGroup.getId(), menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격이 null 이거나 음수이면 안됩니다.");
        }

        @DisplayName("메뉴가 속한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistMemberGroup() {
            final Menu menu = new Menu("두마리메뉴", BigDecimal.valueOf(32000), Long.MAX_VALUE, menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴가 속한 메뉴그룹이 존재하지 않습니다.");
        }

        @DisplayName("메뉴에 등록하려는 상품이 존재하지 않는 상품이면 예외가 발생한다.")
        @Test
        void create_Exception_NonExistProduct() {
            final List<MenuProduct> menuProducts = List.of(createMenuProduct(null, Long.MAX_VALUE));
            final Menu menu = new Menu("두마리메뉴", BigDecimal.valueOf(32000), menuGroup.getId(), menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴에 등록하려는 상품이 존재하지 않습니다.");
        }

        @DisplayName("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 예외가 발생한다.")
        @Test
        void create_Exception_SumOfPrice() {
            final BigDecimal totalPrice = product1.getPrice().add(product2.getPrice());
            final Menu menu = new Menu("원플원", totalPrice.add(BigDecimal.ONE), menuGroup.getId(), menuProducts);

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 안됩니다.");
        }
    }

    @DisplayName("모든 메뉴와 메뉴에 속한 메뉴상품들을 불러온다.")
    @Test
    void list() {
        final List<Menu> menus = menuService.list();

        final Optional<Menu> foundMenu = menus.stream()
                .filter(it -> it.getId().equals(menu.getId()))
                .findAny();
        assertAll(
                () -> assertThat(foundMenu).isPresent(),
                () -> assertThat(foundMenu.get().getMenuProducts())
                        .usingElementComparatorIgnoringFields("seq")
                        .containsExactly(menuProducts.get(0), menuProducts.get(1)));
    }
}
