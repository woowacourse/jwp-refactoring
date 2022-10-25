package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class MenuServiceTest {

    private final ProductService productService;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    MenuServiceTest(ProductService productService, MenuService menuService,
                    MenuGroupService menuGroupService) {
        this.productService = productService;
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 메뉴를_생성한다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), menuGroup.getId(), List.of(후라이드, 양념));

        Menu actual = menuService.create(menu);
        assertThat(actual).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", null, menuGroup.getId(), List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(-1), menuGroup.getId(), List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_메뉴그룹이_존재하지_않는_경우_예외를_발생시킨다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), -1L, List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품이_존재하지_않는_경우_예외를_발생시킨다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 존재하지_않는_상품 = new MenuProduct(2L, null, -1L, 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), menuGroup.getId(), List.of(후라이드, 존재하지_않는_상품));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_상품의_가격의_합과_메뉴의_가격이_다를_경우_예외를_발생시킨다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_001), menuGroup.getId(), List.of(후라이드, 양념));

        assertThatThrownBy(() -> menuService.create(menu))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_조회한다() {
        List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
