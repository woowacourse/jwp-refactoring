package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.맵슐랭;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest {

    private MenuDao menuDao;
    private MenuGroupDao menuGroupDao;
    private ProductDao productDao;
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuDao = MenuFixture.setUp().getMenuDao();
        menuGroupDao = MenuGroupFixture.setUp().getMenuGroupDao();
        productDao = ProductFixture.setUp().getProductDao();
        menuService = new MenuService(menuDao, menuGroupDao, productDao);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        final BigDecimal menuPrice = new BigDecimal(20_000);
        final MenuCreateRequest request = MenuFixture.createMenuRequest("맵슐랭순살", menuPrice, 한마리메뉴,
                List.of(MenuProductFixture.createMenuProduct(맵슐랭)));

        final MenuResponse menuResponse = menuService.create(request);

        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo("맵슐랭순살"),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuPrice)
        );
    }

    @Test
    @DisplayName("메뉴 그룹이 올바르지 않은 경우 예외 발생")
    void whenInvalidMenuGroup() {
        long invalidMenuGroupId = 99999L;
        final MenuCreateRequest request = MenuFixture.createMenuRequest(invalidMenuGroupId);

        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 목록을 가져온다.")
    void getList() {
        final List<MenuResponse> expectedMenus = MenuFixture.setUp()
                .getFixtures();
        final List<MenuResponse> menus = menuService.list();

        assertAll(
                () -> assertThat(menus).hasSameSizeAs(expectedMenus),
                () -> assertThat(menus).usingRecursiveComparison()
                        .isEqualTo(expectedMenus)
        );
    }
}
