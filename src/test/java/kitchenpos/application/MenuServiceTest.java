package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.*;
import static kitchenpos.application.fixture.MenuGroupFixtures.*;
import static kitchenpos.application.fixture.MenuProductFixtures.*;
import static kitchenpos.application.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest {

    private final MenuService menuService;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuServiceTest(final MenuService menuService, final ProductService productService,
                           final MenuGroupService menuGroupService) {
        this.menuService = menuService;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void menu를_생성한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());

        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));

        Menu actual = menuService.create(menu);

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo(menu.getName());
            assertThat(actual.getPrice().compareTo(menu.getPrice())).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId());
            assertThat(actual.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void price가_null인_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = generateMenu("후라이드치킨", null, menuGroup.getId());

        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "price가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void price가_0미만인_경우_예외를_던진다(final int price) {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(price), menuGroup.getId());

        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_menuGroupId인_경우_예외를_던진다() {
        Menu menu = 후라이드치킨(0L);

        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_menu에_속한_product의_총_price보다_큰_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = generateMenu("후라이드치킨", BigDecimal.valueOf(20000), menuGroup.getId());

        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_list를_조회한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());

        Menu menu_후라이드치킨 = 후라이드치킨(menuGroup.getId());
        Product product_후라이드 = productService.create(후라이드());
        menu_후라이드치킨.setMenuProducts(List.of(generateMenuProduct(product_후라이드.getId(), 1)));
        menuService.create(menu_후라이드치킨);

        Menu menu_양념치킨 = 양념치킨(menuGroup.getId());
        Product product_양념치킨 = productService.create(양념치킨());
        menu_양념치킨.setMenuProducts(List.of(generateMenuProduct(product_양념치킨.getId(), 1)));
        menuService.create(menu_양념치킨);

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(2);
    }
}
