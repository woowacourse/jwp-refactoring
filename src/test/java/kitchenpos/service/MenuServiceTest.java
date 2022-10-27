package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.ProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        Menu menu = createMenu();

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertEqualToMenu(menu, savedMenu);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        Menu menu = createMenu();
        Menu savedMenu = menuService.create(menu);

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).contains(savedMenu);
    }

    private Menu createMenu() {
        return createMenu(48000);
    }

    private Menu createMenu(int priceValue) {
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");
        Long menuGroupId = menuGroupService.create(menuGroup).getId();

        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        Long productId = productService.create(product).getId();

        return new Menu("후라이드+후라이드+후라이드", new BigDecimal(priceValue), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
    }

    private void assertEqualToMenu(final Menu menu, final Menu savedMenu) {
        assertThat(savedMenu).usingRecursiveComparison()
            .ignoringFields("id", "price", "menuProducts")
            .isEqualTo(menu);
        assertThat(savedMenu.getPrice()).isCloseTo(menu.getPrice(), Percentage.withPercentage(0.0001));
        assertThat(savedMenu.getMenuProducts()).isNotNull();
    }
}
