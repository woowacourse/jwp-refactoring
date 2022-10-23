package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends IntegrationTest {

    @Autowired
    private MenuService menuService;

    private List<MenuProduct> menuProducts = List.of(
            new MenuProduct(null, 1L, 1),
            new MenuProduct(null, 2L, 1));

    @DisplayName("메뉴를 정상등록 할 수 있다.")
    @Test
    void create() {
        final Menu menu = new Menu("두마리메뉴", BigDecimal.valueOf(32000), 1L, menuProducts);

        final Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴의 가격이 null 이거나 음수이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_Exception_Price(BigDecimal price) {
        final Menu menu = new Menu("두마리메뉴", price, 1L, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴가 속한 메뉴그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_Exception_NonExistMemberGroup() {
        final Menu menu = new Menu("두마리메뉴", BigDecimal.valueOf(32000), 5L, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴의 상품들 * 가격보다 크면 예외가 발생한다.")
    @Test
    void create_Exception_SumOfPrice() {
        final Menu menu = new Menu("원플원", BigDecimal.valueOf(32001), 1L, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴와 메뉴에 속한 메뉴상품들을 불러온다.")
    @Test
    void list() {
        final List<Menu> menus = menuService.list();

        for (Menu menu : menus) {
            assertAll(
                    () -> assertThat(menu.getId()).isNotNull(),
                    () -> assertThat(menu.getMenuProducts()).isNotEmpty()
            );
        }
    }
}
