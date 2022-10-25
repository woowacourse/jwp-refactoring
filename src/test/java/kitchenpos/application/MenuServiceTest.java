package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.INVALID_MENU_PRODUCTS;
import static kitchenpos.application.fixture.MenuFixture.MENU_EXPENSIVE_PRICE;
import static kitchenpos.application.fixture.MenuFixture.MENU_INVALID_PRICE;
import static kitchenpos.application.fixture.MenuFixture.MENU_NAME;
import static kitchenpos.application.fixture.MenuFixture.MENU_PRICE;
import static kitchenpos.application.fixture.MenuFixture.MENU_PRODUCTS;
import static kitchenpos.application.fixture.MenuFixture.UNSAVED_MENU;
import static kitchenpos.application.fixture.MenuGroupFixture.MENU_GROUP_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu savedMenu = menuService.create(UNSAVED_MENU);
        Optional<Menu> foundMenu = menuDao.findById(savedMenu.getId());

        assertThat(foundMenu).isPresent();
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() {
        int numberOfSavedMenuBeforeCreate = menuService.list().size();
        menuService.create(UNSAVED_MENU);

        int numberOfSavedMenu = menuService.list().size();

        assertThat(numberOfSavedMenuBeforeCreate + 1).isEqualTo(numberOfSavedMenu);
    }

    @DisplayName("메뉴에 가격 이름 메뉴그룹아이디 중 하나라도 없으면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource({"argsOfCreateExceptionField"})
    void create_Exception_Field(Menu invalidMenu) {
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(Exception.class);
    }

    static Stream<Arguments> argsOfCreateExceptionField() {
        return Stream.of(
                Arguments.of(new Menu(null, MENU_PRICE, MENU_GROUP_ID, Collections.emptyList())),
                Arguments.of(new Menu(MENU_NAME, null, MENU_GROUP_ID, Collections.emptyList())),
                Arguments.of(new Menu(MENU_NAME, MENU_PRICE, null, Collections.emptyList()))
        );
    }

    @DisplayName("메뉴의 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_Exception_Invalid_Price() {
        Menu menu = new Menu(MENU_NAME, MENU_INVALID_PRICE, MENU_GROUP_ID, MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품들의 가격 합보다 메뉴의 가격이 비싸면 예외가 발생한다.")
    @Test
    void create_Exception_TOO_EXPENSIVE_PRICE() {
        Menu menu = new Menu(MENU_NAME, MENU_EXPENSIVE_PRICE, MENU_GROUP_ID, MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품들 중 유효하지 않은 상품이 있으면 예외가 발생한다.")
    @Test
    void create_Exception_Invalid_Product() {
        Menu menu = new Menu(MENU_NAME, MENU_PRICE, MENU_GROUP_ID, INVALID_MENU_PRODUCTS);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
