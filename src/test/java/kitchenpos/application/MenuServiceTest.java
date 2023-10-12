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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "classpath:data/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void create() {
        // given
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("Group1"));
        final Product product = productService.create(new Product("Product1", BigDecimal.valueOf(10000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId(), List.of(menuProduct));

        // when
        final Menu result = menuService.create(menu);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 99999})
    void create_priceException(int price) {
        // given
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("Group1"));
        final Product product = productService.create(new Product("Product1", BigDecimal.valueOf(10000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(price), menuGroup.getId(), List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_menuGroupException() {
        // given
        final Product product = productService.create(new Product("Product1", BigDecimal.valueOf(10000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        final Menu menu = new Menu("후라이드+후라이드", BigDecimal.valueOf(-1), -1L, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("Group1"));
        final Product product = productService.create(new Product("Product1", BigDecimal.valueOf(10000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);

        menuService.create(new Menu("Menu1", BigDecimal.valueOf(1000), menuGroup.getId(), List.of(menuProduct)));
        menuService.create(new Menu("Menu2", BigDecimal.valueOf(2000), menuGroup.getId(), List.of(menuProduct)));

        // when
        final List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
