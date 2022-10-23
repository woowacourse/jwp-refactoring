package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuService 클래스의")
class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("create 메서드는 메뉴를 생성한다.")
    void create() {
        // given
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup("반마리치킨"));
        Product product = productService.create(createProduct("크림치킨", BigDecimal.valueOf(10000.00)));
        MenuProduct menuProduct = createMenuProduct(product.getId(), 1);
        Menu menu = createMenu("크림치킨", BigDecimal.valueOf(10000.00), menuGroup.getId(),
                Collections.singletonList(menuProduct));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Optional<Menu> actual = menuDao.findById(savedMenu.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("list 메서드는 모든 메뉴를 조회한다.")
    void list() {
        // given
        MenuGroup menuGroup = menuGroupService.create(createMenuGroup("반마리치킨"));
        Product product = productService.create(createProduct("크림치킨", BigDecimal.valueOf(15000.00)));
        saveMenu("크림치킨", menuGroup, product);
        saveMenu("크림어니언치킨", menuGroup, product);
        saveMenu("크림치즈치킨", menuGroup, product);

        // when
        List<Menu> menus = menuService.list();
        for (Menu menu : menus) {
            System.out.println("menu.getName() = " + menu.getName());
        }

        // then
        assertThat(menus).hasSize(3);
    }
}
