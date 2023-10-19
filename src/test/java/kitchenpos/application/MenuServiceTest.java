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
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.ProductFixture;
import kitchenpos.supports.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("메뉴 서비스 테스트")
@IntegrationTest
class MenuServiceTest {

    private static final long INVALID_ID = -1L;

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    private Menu setUpMenu() {
        final Product product = productService.create(ProductFixture.create());
        final MenuGroup menuGroup = menuGroupService.create(MenuGroupFixture.create());

        return MenuFixture.of(menuGroup.getId(), List.of(product));
    }

    @Nested
    @DisplayName("메뉴를 생성할 때")
    class Create {

        @DisplayName("정상적으로 생성할 수 있다")
        @ParameterizedTest
        @ValueSource(ints = {0, 1000})
        void success(int price) {
            // given
            final Menu menu = setUpMenu();
            menu.setPrice(BigDecimal.valueOf(price));

            // when
            final Menu savedMenu = menuService.create(menu);

            // then
            assertSoftly(softly -> {
                assertThat(savedMenu.getId()).isPositive();
                assertThat(savedMenu.getName()).isEqualTo(menu.getName());
                assertThat(savedMenu.getMenuProducts().get(0).getMenuId()).isPositive();
            });
        }

        @DisplayName("가격이 없으면 예외처리 한다")
        @Test
        void throwExceptionWhenPriceIsNull() {
            // given
            final Menu menu = setUpMenu();
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
            final Menu menu = setUpMenu();
            menu.setPrice(BigDecimal.valueOf(price));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidMenuGroup() {
            // given
            final Menu menu = setUpMenu();
            menu.setMenuGroupId(INVALID_ID);

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 상품이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidProduct() {
            // given
            final Menu menu = setUpMenu();
            final MenuProduct menuProduct = new MenuProduct();
            menuProduct.setQuantity(2);
            menuProduct.setProductId(INVALID_ID);
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
            final BigDecimal productPrice = product.getPrice();
            final BigDecimal productCount = BigDecimal.valueOf(2);
            menu.setPrice(productPrice.multiply(productCount).add(BigDecimal.ONE));
            menu.setName("상품+상품");
            menu.setMenuGroupId(menuGroup.getId());
            menu.setMenuProducts(List.of(menuProduct));

            // then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void findAllMenus() {
        // given
        final Menu menu = setUpMenu();
        final Menu savedMenu = menuService.create(menu);

        // when
        final List<Menu> list = menuService.list();

        // then
        assertSoftly(softly -> {
            assertThat(list).hasSize(1);
            assertThat(list.get(0))
                    .usingComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                    .usingRecursiveComparison()
                    .isEqualTo(savedMenu);
        });
    }
}
