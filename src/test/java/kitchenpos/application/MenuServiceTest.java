package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
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
        menuGroup = new MenuGroup("menu group");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        product = new Product("product", new BigDecimal(PRODUCT_PRICE));
        product = testFixtureBuilder.buildProduct(product);

        menuProduct = new MenuProduct(product.getId(), 1);
    }

    @DisplayName("메뉴 생성 테스트")
    @Nested
    class MenuCreateTest {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void createMenu() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", new BigDecimal(100), menuGroup.getId(), List.of(new MenuCreateRequest.MenuProductCreate(product.getId(), 3L)));

            //when
            final Long id = menuService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(id).isNotNull();
            });
        }

        @DisplayName("메뉴의 가격이 음수면 실패한다.")
        @Test
        void menuCreateFailWhenPriceLessThenZero() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", new BigDecimal(-1), -1L, List.of(new MenuCreateRequest.MenuProductCreate(product.getId(), 3L)));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴그룹이면 실패한다.")
        @Test
        void menuCreateFailWhenNotExistMenuGroup() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", new BigDecimal(100), -1L, List.of(new MenuCreateRequest.MenuProductCreate(product.getId(), 3L)));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 제품이면 실패한다.")
        @Test
        void menuCreateFailWhenNotExistProduct() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", new BigDecimal(100), menuGroup.getId(), List.of(new MenuCreateRequest.MenuProductCreate(-1L, 3L)));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 null이면 실패한다.")
        @Test
        void menuCreateFailWhenPriceIsNull() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", null, menuGroup.getId(), List.of(new MenuCreateRequest.MenuProductCreate(product.getId(), 3L)));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 제품들의 가격의 합의 초과면 실패한다.")
        @Test
        void menuCreateFailWhenMenuPriceGraterThenProductsPriceSum() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest("name", new BigDecimal(PRODUCT_PRICE + 1000), menuGroup.getId(), List.of(new MenuCreateRequest.MenuProductCreate(product.getId(), 1L)));

            // when & then
            assertThatThrownBy(() -> menuService.create(request))
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
            final Menu menu = new Menu("name", new BigDecimal(1000), menuGroup.getId(), List.of(menuProduct));

            final Menu expected = testFixtureBuilder.buildMenu(menu);

            //when
            final List<MenuResponse> actual = menuService.list();

            //then
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
            });
        }
    }
}
