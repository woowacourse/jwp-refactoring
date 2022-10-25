package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class MenuServiceTest {

    private final MenuService menuService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final TestFixture testFixture;

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public MenuServiceTest(MenuService menuService, MenuGroupService menuGroupService, ProductService productService,
                           TestFixture testFixture) {
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.testFixture = testFixture;
    }

    @BeforeEach
    void setUp() {
        Product savedProduct = productService.create(testFixture.삼겹살());
        MenuGroup savedMenuGroup = menuGroupService.create(testFixture.삼겹살_종류());
        MenuProduct menuProduct = new MenuProduct(savedProduct.getId(), 1L);

        this.menuGroup = savedMenuGroup;
        this.menuProducts = List.of(menuProduct);
    }

    @DisplayName("메뉴의 가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void menuWithNullPrice() {
        Menu menu = new Menu("맛있는 메뉴", null, menuGroup.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 음수라면 예외가 발생한다.")
    @Test
    public void menuWithNegativePrice() {
        Menu menu = new Menu("맛있는 메뉴", BigDecimal.valueOf(-1), menuGroup.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않다면 예외가 발생한다.")
    @Test
    public void menuGroupNotSaved() {
        Menu menu = new Menu("맛있는 메뉴", BigDecimal.valueOf(1000), null, menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 내에 존재하지 않는 상품이 있는 경우 예외가 발생한다.")
    @Test
    public void menuProductNotContained() {
        List<MenuProduct> copiedProducts = new ArrayList<>(menuProducts);
        MenuProduct fakeProduct = new MenuProduct(-1L, 10);
        copiedProducts.add(fakeProduct);
        Menu menu = new Menu("맛있는 메뉴", BigDecimal.valueOf(1000), null, copiedProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격 합보다 메뉴의 가격이 비싸다면 예외가 발생한다.")
    @Test
    public void menuProductPriceDoesNotExceedTotalSum() {
        Menu menu = new Menu("맛있는 메뉴", BigDecimal.valueOf(1500), menuGroup.getId(), menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 메뉴들을 출력할 수 있다.")
    @Test
    public void menulist() {
        Menu menu1 = new Menu("맛있는 메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);
        Menu menu2 = new Menu("적당히 맛있는 메뉴", BigDecimal.valueOf(1000), menuGroup.getId(), menuProducts);
        menuService.create(menu1);
        menuService.create(menu2);

        assertThat(menuService.list()).hasSize(2);
    }
}
