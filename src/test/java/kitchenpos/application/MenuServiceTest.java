package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuServiceTest extends ServiceTest {

    private static final int PRODUCT_PRICE = 1000;
    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private Product product;
    private MenuProduct menuProduct;

    @BeforeEach
    void beforeEach() {
        menuGroup = new MenuGroup();
        menuGroup.setName("menuGroup");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        product = new Product();
        product.setName("product");
        product.setPrice(new BigDecimal(PRODUCT_PRICE));
        product = testFixtureBuilder.buildProduct(product);

        menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);
        menuProduct.setSeq(1L);
    }

    @DisplayName("메뉴 생성 테스트")
    @Nested
    class MenuCreateTest {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void createMenu() {
            //given
            final Menu expected = new Menu();
            expected.setPrice(new BigDecimal(100));
            expected.setMenuGroupId(menuGroup.getId());
            expected.setName("menu");
            expected.setMenuProducts(List.of(menuProduct));

            //when
            final Menu actual = menuService.create(expected);

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isNotNull();
            });
        }

        @DisplayName("메뉴의 가격이 음수면 실패한다.")
        @Test
        void menuCreateFailWhenPriceLessThenZero() {
            //given
            final Menu menu = new Menu();
            menu.setPrice(new BigDecimal(-1));
            menu.setMenuGroupId(menuGroup.getId());
            menu.setName("menu");

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴그룹이면 실패한다.")
        @Test
        void menuCreateFailWhenNotExistMenuGroup() {
            //given
            final Menu menu = new Menu();
            menu.setPrice(new BigDecimal(-1));
            menu.setMenuGroupId(-1L);
            menu.setName("menu");

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 제품이면 실패한다.")
        @Test
        void menuCreateFailWhenNotExistProduct() {
            //given
            final Menu menu = new Menu();
            menu.setPrice(new BigDecimal(-1));
            menu.setMenuGroupId(menuGroup.getId());
            menu.setName("menu");

            final MenuProduct notExistProduct = new MenuProduct();
            notExistProduct.setProductId(-1L);
            menu.setMenuProducts(List.of(notExistProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 null이면 실패한다.")
        @Test
        void menuCreateFailWhenPriceIsNull() {
            //given
            final Menu menu = new Menu();
            menu.setPrice(null);
            menu.setMenuGroupId(menuGroup.getId());
            menu.setName("menu");

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 제품들의 가격의 합의 초과면 실패한다.")
        @Test
        void menuCreateFailWhenMenuPriceGraterThenProductsPriceSum() {
            //given
            final Menu menu = new Menu();
            menu.setMenuGroupId(menuGroup.getId());
            menu.setName("menu");
            menu.setPrice(new BigDecimal(PRODUCT_PRICE + 10000));
            menu.setMenuProducts(List.of(menuProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 조회 테스트")
    @Nested
    class MenuFindTest {

        @DisplayName("menu를 전체 조회한다.")
        @Test
        void findAllMenu() {
            //given
            final Menu menu = new Menu();
            menu.setMenuGroupId(menuGroup.getId());
            menu.setName("menu");
            menu.setPrice(new BigDecimal(1000));
            final Menu expected = testFixtureBuilder.buildMenu(menu);

            //when
            final List<Menu> actual = menuService.list();

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
            });
        }
    }
}
