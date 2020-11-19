package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class MenuServiceTest {

    private static final String NEW_MENU_NAME = "후라이드양념간장메뉴";
    private static final BigDecimal NEW_MENU_PRICE = new BigDecimal(48_000L);
    private static final MenuGroup NEW_MENU_GROUP = TestObjectFactory.createMenuGroup("세마리메뉴");

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create() {
        MenuGroup menuGroup = menuGroupService.create(NEW_MENU_GROUP);
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, NEW_MENU_PRICE, menuGroup, new_menu_product);

        Menu savedMenu = menuService.create(menu);

        assertAll(() -> {
            assertThat(savedMenu).isInstanceOf(Menu.class);
            assertThat(savedMenu).isNotNull();
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getPrice()).isNotNull();
            assertThat(savedMenu.getPrice().toBigInteger())
                .isEqualTo(menu.getPrice().toBigInteger());
            assertThat(savedMenu.getMenuGroup()).isNotNull();
            assertThat(savedMenu.getMenuGroup()).isEqualTo(menu.getMenuGroup());
            assertThat(savedMenu.getMenuProducts()).isNotEmpty();
            assertThat(savedMenu.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
        });
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 null일 경우")
    @Test
    void create_IfMenuPriceNull_ThrowException() {
        MenuGroup menuGroup = menuGroupService.create(NEW_MENU_GROUP);
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, null, menuGroup, new_menu_product);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 0 이하일 경우")
    @Test
    void create_IfMenuPriceIsNotPositive_ThrowException() {
        MenuGroup menuGroup = menuGroupService.create(NEW_MENU_GROUP);
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, new BigDecimal(-1L), menuGroup, new_menu_product);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - groupId가 메뉴 그룹에 존재하지 않는 경우")
    @Test
    void create_IfGroupIdNotExist_ThrowException() {
        MenuGroup invalidMenuGroup = new MenuGroup();
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        invalidMenuGroup.setId(0L);
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, NEW_MENU_PRICE, invalidMenuGroup, new_menu_product);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 모든 메뉴 가격 합을 초과할 경우")
    @Test
    void create_IfInvalidMenuPrice_ThrowException() {
        MenuGroup menuGroup = menuGroupService.create(NEW_MENU_GROUP);
        List<MenuProduct> new_menu_product =
            Arrays.asList(menuProductRepository.findAllByMenuId(1L).get(0),
                menuProductRepository.findAllByMenuId(2L).get(0),
                menuProductRepository.findAllByMenuId(5L).get(0));

        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, new BigDecimal(50_000L), menuGroup, new_menu_product);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴 리스트를 조회한다.")
    @Test
    void list() {
        List<Menu> menuList = menuService.list();

        assertAll(() -> {
            assertThat(menuList).isNotEmpty();
            assertThat(menuList).hasSize(6);
        });
    }
}
