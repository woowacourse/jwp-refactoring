package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.MenuFixtures;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuServiceTest {

    private MenuService menuService;

    @Autowired
    public MenuServiceTest(MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void create() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest();
        // when
        Menu createdMenu = menuService.create(request);
        // then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @Test
    void createMenuWithNullPrice() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest((BigDecimal) null);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithNegativePrice() {
        // given
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(BigDecimal.valueOf(-1L));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidMenuGroup() {
        // given
        long invalidMenuGroupId = 999L;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(invalidMenuGroupId);

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithInvalidProduct() {
        // given
        long invalidProductId = 999L;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(
                List.of(MenuFixtures.createMenuProductCreateRequest(null, invalidProductId, 3))
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createMenuWithMoreExpensivePrice() {
        // given
        int expensivePrice = 100_000;
        MenuCreateRequest request = MenuFixtures.createMenuCreateRequest(BigDecimal.valueOf(expensivePrice));

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given & when
        List<Menu> menus = menuService.list();
        for (Menu menu : menus) {
            List<MenuProduct> menuProducts = menu.getMenuProducts();
            for (MenuProduct menuProduct : menuProducts) {
                BigDecimal bigDecimal = menuProduct.calculatePrice();
                System.out.println("bigDecimal = " + bigDecimal);
            }
        }
        // then
        int defaultSize = 6;
        assertThat(menus).hasSize(defaultSize);
    }
}
