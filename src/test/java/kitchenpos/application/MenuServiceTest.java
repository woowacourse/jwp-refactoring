package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
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

    private static final String NEW_MENU_NAME = "후라이드양념두마리메뉴";
    private static final BigDecimal NEW_MENU_PRICE = new BigDecimal(31_000L);
    private static final Long NEW_MENU_GROUP_ID = 1L;
    private static final List<MenuProduct> NEW_MENU_PRODUCT =
        Collections.singletonList(TestObjectFactory.createMenuProduct(1L, 2L));

    @Autowired
    private MenuService menuService;

    @DisplayName("새로운 메뉴를 생성한다.")
    @Test
    void create() {
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, NEW_MENU_PRICE, NEW_MENU_GROUP_ID, NEW_MENU_PRODUCT);
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
            assertThat(savedMenu.getMenuGroupId()).isNotNull();
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            assertThat(savedMenu.getMenuProducts()).isNotEmpty();
            assertThat(savedMenu.getMenuProducts().size()).isEqualTo(menu.getMenuProducts().size());
        });
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 null일 경우")
    @Test
    void create_IfMenuPriceNull_ThrowException() {
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, null, NEW_MENU_GROUP_ID, NEW_MENU_PRODUCT);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 0 이하일 경우")
    @Test
    void create_IfMenuPriceIsNotPositive_ThrowException() {
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, new BigDecimal(-1L), NEW_MENU_GROUP_ID, NEW_MENU_PRODUCT);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - groupId가 메뉴 그룹에 존재하지 않는 경우")
    @Test
    void create_IfGroupIdNotExist_ThrowException() {
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, NEW_MENU_PRICE, 5L, NEW_MENU_PRODUCT);

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성한다. - 메뉴 가격이 모든 메뉴 가격 합을 초과할 경우")
    @Test
    void create_IfInvalidMenuPrice_ThrowException() {
        Menu menu = TestObjectFactory
            .createMenu(NEW_MENU_NAME, new BigDecimal(34_000L), NEW_MENU_GROUP_ID,
                NEW_MENU_PRODUCT);

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
