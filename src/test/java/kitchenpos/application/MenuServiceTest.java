package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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

            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    BigDecimal.valueOf(10_000),
                    menuGroupId,
                    List.of(
                            new MenuProductRequest(product1.getId(), 2L),
                            new MenuProductRequest(product2.getId(), 4L)
                    )
            );

            // when
            final MenuResponse actual = menuService.create(request);

            // then
            softly.assertThat(actual.getName()).isEqualTo(request.getName());
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
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));
            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    null,
                    menuGroupId,
                    List.of(
                            new MenuProductRequest(product1.getId(), 2L),
                            new MenuProductRequest(product2.getId(), 4L)
                    )
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 수 없다.")
        void create_priceIsNegative_exception() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));

            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    BigDecimal.valueOf(-1),
                    menuGroupId,
                    List.of(
                            new MenuProductRequest(product1.getId(), 2L),
                            new MenuProductRequest(product2.getId(), 4L)
                    )
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 상품의 상품이 존재해야 한다.")
        void create_productNotExist_exception() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product = saveProduct("간장치킨", BigDecimal.valueOf(2_000));

            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    BigDecimal.valueOf(10_000),
                    menuGroupId,
                    List.of(
                            new MenuProductRequest(product.getId(), 2L),
                            new MenuProductRequest(999L, 4L)
                    )
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격은 각 메뉴 상품의 상품 금액의 총 합보다 클 수 없다.")
        void create_priceGraterThanSumOfAmount_exception() {
            // given
            final Long menuGroupId = saveMenuGroup("치킨").getId();
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));

            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    BigDecimal.valueOf(10_001),
                    menuGroupId,
                    List.of(
                            new MenuProductRequest(product1.getId(), 2L),
                            new MenuProductRequest(product2.getId(), 4L)
                    )
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 그룹이 존재해야 한다.")
        void create_menuGroupNotExist_exception() {
            // given
            final Product product1 = saveProduct("간장치킨", BigDecimal.valueOf(2_000));
            final Product product2 = saveProduct("앙념치킨", BigDecimal.valueOf(1_500));

            final MenuCreateRequest request = new MenuCreateRequest(
                    "반반치킨",
                    BigDecimal.valueOf(10_000),
                    999L,
                    List.of(
                            new MenuProductRequest(product1.getId(), 2L),
                            new MenuProductRequest(product2.getId(), 4L)
                    )
            );

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
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
                    new MenuProduct(chicken1.getId(), 2L),
                    new MenuProduct(chicken2.getId(), 4L));

            final Product sushi1 = saveProduct("연어초밥");
            final Product sushi2 = saveProduct("광어초밥");
            final Product sushi3 = saveProduct("참치초밥");
            final MenuGroup sushiMenuGroup = saveMenuGroup("초밥");
            final Menu sushiMenu = saveMenu("모둠초밥", BigDecimal.valueOf(15_000), sushiMenuGroup,
                    new MenuProduct(sushi1.getId(), 3L),
                    new MenuProduct(sushi2.getId(), 2L),
                    new MenuProduct(sushi3.getId(), 1L));

            // when
            final List<MenuResponse> actual = menuService.list();

            // then
            assertThat(actual).extracting("name", "price", "menuGroupId")
                    .containsExactly(
                            tuple(chickenMenu.getName(), new BigDecimal("10000.00"), chickenMenuGroup.getId()),
                            tuple(sushiMenu.getName(), new BigDecimal("15000.00"), sushiMenuGroup.getId())
                    );
        }
    }
}
