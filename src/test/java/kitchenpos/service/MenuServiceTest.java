package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        Menu menu = createMenu();

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        assertThat(savedMenu).usingRecursiveComparison()
            .ignoringFields("id", "menuProducts", "price")
            .isEqualTo(menu);
    }

    @Test
    @DisplayName("가격이 음수이면 예외를 던진다.")
    void create_price_negative() {
        // given
        Menu menu = createMenu(-1);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
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

        Product product = new Product("후라이드", BigDecimal.valueOf(16000));
        Long productId = productService.create(product).getId();

        return new Menu("후라이드+후라이드+후라이드", new BigDecimal(priceValue), menuGroupId,
            List.of(new MenuProduct(productId, 3)));
    }
}
