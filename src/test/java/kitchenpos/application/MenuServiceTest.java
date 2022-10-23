package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

@DisplayName("MenuService의")
class MenuServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("메뉴를 생성한다.")
        void create_validMenu_success() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));
            final List<MenuProduct> menuProducts = getMenuProducts(
                    Pair.of(product1, 2L),
                    Pair.of(product2, 4L)
            );

            final Menu expected = new Menu();
            expected.setName("반반치킨");
            expected.setPrice(BigDecimal.valueOf(10_000));
            expected.setMenuGroupId(menuGroupId);
            expected.setMenuProducts(menuProducts);

            // when
            final Menu actual = menuService.create(expected);

            // then
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10_000));
            softly.assertThat(actual.getMenuGroupId()).isEqualTo(menuGroupId);
            softly.assertThat(actual.getMenuProducts()).extracting("productId", "quantity")
                    .containsExactly(
                            tuple(product1.getId(), 2L),
                            tuple(product2.getId(), 4L)
                    );
            softly.assertAll();
        }

        @Test
        @DisplayName("가격이 null일 수 없다.")
        void create_priceIsNull_exception() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(null);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 수 없다.")
        void create_priceIsNegative_exception() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(-1));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 상품의 상품이 존재해야 한다.")
        void create_productNotExist_exception() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            product1.setId(999L);
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));
            final List<MenuProduct> menuProducts = getMenuProducts(
                    Pair.of(product1, 2L),
                    Pair.of(product2, 4L)
            );

            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(10_001));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(menuProducts);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격은 각 메뉴 상품의 상품 금액의 총 합보다 클 수 없다.")
        void create_priceGraterThanSumOfAmount_exception() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));
            final List<MenuProduct> menuProducts = getMenuProducts(
                    Pair.of(product1, 2L),
                    Pair.of(product2, 4L)
            );

            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.valueOf(10_001));
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(menuProducts);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 그룹이 존재해야 한다.")
        void create_menuGroupNotExist_exception() {
            // given
            final Menu menu = new Menu();
            menu.setPrice(BigDecimal.ONE);
            menu.setMenuGroupId(999L);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("메뉴 목록을 조회한다.")
        void list_savedMenus_success() {
            // given
            final Product chicken1 = saveProduct("간장치킨");
            final Product chicken2 = saveProduct("앙념치킨");
            final MenuGroup chickenMenuGroup = saveMenuGroup("치킨");
            final Menu chickenMenu = saveMenu("반반치킨", BigDecimal.valueOf(10_000), chickenMenuGroup,
                    Pair.of(chicken1, 2L), Pair.of(chicken2, 4L));

            final Product sushi1 = saveProduct("연어초밥");
            final Product sushi2 = saveProduct("광어초밥");
            final Product sushi3 = saveProduct("참치초밥");
            final MenuGroup sushiMenuGroup = saveMenuGroup("초밥");
            final Menu sushiMenu = saveMenu("모둠초밥", BigDecimal.valueOf(15_000), sushiMenuGroup,
                    Pair.of(sushi1, 3L), Pair.of(sushi2, 2L), Pair.of(sushi3, 1L));

            // when
            final List<Menu> actual = menuService.list();

            // then
            assertThat(actual).extracting("name", "price", "menuGroupId")
                    .contains(
                            tuple(chickenMenu.getName(), chickenMenu.getPrice(), chickenMenuGroup.getId()),
                            tuple(sushiMenu.getName(), sushiMenu.getPrice(), sushiMenuGroup.getId())
                    );
        }
    }
}
