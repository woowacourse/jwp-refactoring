package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuProductCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.dao.InMemoryMenuDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static final Long 맵슐랭 = 1L;
    public static final Long 허니콤보 = 2L;

    private final MenuDao menuDao;
    private List<MenuResponse> fixtures;

    public MenuFixture(final MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public static MenuFixture setUp() {
        final MenuFixture menuFixture = new MenuFixture(new InMemoryMenuDao());
        menuFixture.fixtures = menuFixture.createMenus();
        return menuFixture;
    }

    public static MenuCreateRequest createMenuRequest(final String menuName, final BigDecimal price, final Long menuGroupId,
                                               final List<MenuProductCreateRequest>menuProductsRequest) {
        return new MenuCreateRequest(
                menuName,
                price,
                menuGroupId,
                menuProductsRequest
        );
    }

    public static MenuCreateRequest createMenuRequest(final Long menuGroupId) {
        return new MenuCreateRequest("맛있는메뉴", BigDecimal.valueOf(20_000L), menuGroupId,
                Collections.singletonList(new MenuProductCreateRequest(1L,1L)));
    }

    public static MenuCreateRequest createMenuRequestByPrice(final BigDecimal price) {
        return new MenuCreateRequest("맛있는메뉴", price, 1L,
                Collections.singletonList(new MenuProductCreateRequest(1L, 1L)));
    }

    private List<MenuResponse> createMenus() {
        return List.of(
                MenuResponse.from(
                        saveMenu(MenuGroupFixture.한마리메뉴, "맵슐랭", BigDecimal.valueOf(21_000L), Collections.singletonList(
                                new MenuProduct(1L, 1L, BigDecimal.valueOf(21_000L))))),
                MenuResponse.from(
                        saveMenu(MenuGroupFixture.한마리메뉴, "허니콤보", BigDecimal.valueOf(20_000L), Collections.singletonList(
                                new MenuProduct(2L, 1L, BigDecimal.valueOf(20_000L)))))
        );
    }

    private Menu saveMenu(final Long menuGroupId, final String menuName, final BigDecimal price, final List<MenuProduct> menuProduct) {
        return menuDao.save(createMenu(menuName, price, menuGroupId, menuProduct));
    }

    private Menu createMenu(final String menuName, final BigDecimal price, final Long menuGroupId,
                            final List<MenuProduct> menuProducts) {
        return new Menu(menuName, price, menuGroupId, menuProducts);
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

    public List<MenuResponse> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }
}
