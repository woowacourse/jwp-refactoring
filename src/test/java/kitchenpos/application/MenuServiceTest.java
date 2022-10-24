package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴를_생성할_수_있다() {
        Product product1 = productService.create(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2.getId(), 1);

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("메뉴 그룹"));

        Menu menu = new Menu("메뉴", new BigDecimal(35000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));

        Menu actual = menuService.create(menu);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("메뉴");
            assertThat(actual.getPrice().compareTo(new BigDecimal(35000))).isEqualTo(0);
            assertThat(actual.getMenuGroupId()).isEqualTo(menuGroup.getId());
            assertThat(actual.getMenuProducts()).hasSize(2);
        });
    }

    @Test
    void 메뉴의_가격이_음수인_경우_메뉴를_생성할_수_없다() {
        Product product1 = productService.create(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2.getId(), 1);

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("메뉴 그룹"));

        Menu menu = new Menu("메뉴", new BigDecimal(-1), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴가_존재하는_메뉴_그룹에_속하지_않은_경우_메뉴를_생성할_수_없다() {
        Product product1 = productService.create(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2.getId(), 1);

        Menu menu = new Menu("메뉴", new BigDecimal(35000), 1L,
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_작거나_같은_경우_메뉴를_생성할_수_없다() {
        Product product1 = productService.create(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2.getId(), 1);

        Menu menu = new Menu("메뉴", new BigDecimal(50000), 1L,
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));

        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        Product product1 = productService.create(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productService.create(new Product("상품2", new BigDecimal(20000)));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 2);
        MenuProduct menuProduct2 = new MenuProduct(2L, null, product2.getId(), 1);

        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("메뉴 그룹"));

        Menu menu1 = new Menu("메뉴1", new BigDecimal(35000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));
        Menu menu2 = new Menu("메뉴2", new BigDecimal(38000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2)));

        menuService.create(menu1);
        menuService.create(menu2);

        List<Menu> actual = menuService.list();

        assertThat(actual).hasSize(2);
    }
}
