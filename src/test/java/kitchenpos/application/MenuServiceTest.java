package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final Product product = productService.create(Fixture.PRODUCT);
        menuProduct = new MenuProduct(product, 2);
    }

    @Test
    void create() {
        // given
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup, List.of(menuProduct));

        // when
        final Menu result = menuService.create(menu);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 99999})
    void create_priceException(final int price) {
        // given
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(price), menuGroup, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuGroupException() {
        // given
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(1000), new MenuGroup(), List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final Menu menu1 = menuService.create(
                new Menu("Menu1", BigDecimal.valueOf(1000), menuGroup, List.of(menuProduct)));
        final Menu menu2 = menuService.create(
                new Menu("Menu2", BigDecimal.valueOf(2000), menuGroup, List.of(menuProduct)));

        // when
        final List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(menu1, menu2);
    }
}
