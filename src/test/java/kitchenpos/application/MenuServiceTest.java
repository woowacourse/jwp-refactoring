package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴를_등록할_수_있다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProduct menuProduct = makeMenuProduct(savedProduct);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        final Menu menu = new Menu();
        menu.setName("테스트 메뉴");
        menu.setPrice(new BigDecimal(2000));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // when
        final Menu result = menuService.create(menu);

        // then
        assertThat(result.getName()).isEqualTo(menu.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(menu.getPrice());
    }

    @Test
    void 메뉴가격이_상품가격보다_클_경우_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProduct menuProduct = makeMenuProduct(savedProduct);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        // when
        final Menu menu = new Menu();
        menu.setName("테스트 메뉴");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴가격이_0보다_작은경우_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProduct menuProduct = makeMenuProduct(savedProduct);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");

        // when
        final Menu menu = new Menu();
        menu.setName("테스트 메뉴");
        menu.setPrice(new BigDecimal(-1));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴등록시_메뉴그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProduct menuProduct = makeMenuProduct(savedProduct);

        // when
        final Menu menu = new Menu();
        menu.setName("테스트 메뉴");
        menu.setPrice(new BigDecimal(3000));
        menu.setMenuGroupId(null);
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴목록을_조회할_수_있다() {
        // given
        final Product savedProduct = createProduct(2000);
        final MenuProduct menuProduct = makeMenuProduct(savedProduct);
        final MenuGroup savedMenuGroup = createMenuGroup("테스트 메뉴 그룹");
        final List<MenuProduct> menuProducts = List.of(menuProduct);
        final BigDecimal price = new BigDecimal(2000);

        final Menu menu = new Menu();
        menu.setName("테스트 메뉴");
        menu.setPrice(price);
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);
        menuService.create(menu);

        // when
        final List<Menu> results = menuService.list();
        final Menu savedMenuResult = results.get(0);

        // then
        assertThat(results).hasSize(1);
        assertThat(savedMenuResult).usingRecursiveComparison()
            .ignoringFields("id", "price", "menuProducts")
            .isEqualTo(menu);
        assertThat(savedMenuResult.getPrice()).isEqualByComparingTo(price);
        assertThat(savedMenuResult.getMenuProducts()).usingRecursiveComparison()
            .ignoringFields("seq")
            .isEqualTo(menuProducts);
    }

    private Product createProduct(final int price) {
        final Product product = new Product("테스트 상품", new BigDecimal(price));
        return productRepository.save(product);
    }

    private MenuProduct makeMenuProduct(final Product savedProduct) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        return menuProduct;
    }

    private MenuGroup createMenuGroup(final String groupName) {
        final MenuGroup menuGroup = new MenuGroup(groupName);
        return menuGroupDao.save(menuGroup);
    }
}
