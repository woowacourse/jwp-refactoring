package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("메뉴 서비스 테스트")
@ServiceTest
class MenuServiceTest {

    private final MenuService menuService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuServiceTest(final MenuService menuService, final MenuGroupService menuGroupService,
                           final ProductService productService) {
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    private Menu setUp() {
        // given
        final Product product = productService.create(ProductFixture.create());
        final MenuGroup menuGroup = menuGroupService.create(MenuGroupFixture.create());
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(product.getId());

        final Menu menu = new Menu();
        menu.setPrice(product.getPrice().multiply(BigDecimal.valueOf(2)).subtract(BigDecimal.ONE));
        menu.setName("상품+상품");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        return menu;
    }

    @DisplayName("메뉴를 생성할 수 있다")
    @Test
    void createMenu() {
        //given
        final Menu menu = setUp();

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertSoftly(softly -> {
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isNotNull();
        });
    }

    @DisplayName("메뉴 가격은 0원일 수 있다")
    @Test
    void createMenuWithPriceZero() {
        // given
        final Menu menu = setUp();
        menu.setPrice(BigDecimal.valueOf(0));

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertSoftly(softly -> {
            assertThat(savedMenu.getId()).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isNotNull();
        });
    }

    @DisplayName("가격이 없으면 예외처리 한다")
    @Test
    void throwExceptionWhenPriceIsNull() {
        // given
        final Menu menu = setUp();
        menu.setPrice(null);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격 0 미만이면 예외처리 한다")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2})
    void throwExceptionWhenPriceIsLowerThanZero(int price) {
        // given
        final Menu menu = setUp();
        menu.setPrice(BigDecimal.valueOf(price));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 메뉴 그룹이면 예외처리 한다")
    @Test
    void throwExceptionWhenInvalidMenuGroup() {
        // given
        final Menu menu = setUp();
        menu.setMenuGroupId(-1L);

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품이면 예외처리 한다")
    @Test
    void throwExceptionWhenInvalidProduct() {
        // given
        final Menu menu = setUp();
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(-1L);
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 상품과 수량의 곱의 총 합보다 크면 예외처리 한다.")
    @Test
    void throwExceptionWhenInvalidPrice() {
        // given
        final Product product = productService.create(ProductFixture.create());
        final MenuGroup menuGroup = menuGroupService.create(MenuGroupFixture.create());
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(product.getId());

        final Menu menu = new Menu();
        menu.setPrice(product.getPrice().multiply(BigDecimal.valueOf(2)).add(BigDecimal.ONE));
        menu.setName("상품+상품");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        // then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void findAllMenus() {
        // given
        final Menu menu = setUp();
        menuService.create(menu);

        // when
        final List<Menu> list = menuService.list();

        // then
        assertThat(list).hasSize(1);
    }
}
