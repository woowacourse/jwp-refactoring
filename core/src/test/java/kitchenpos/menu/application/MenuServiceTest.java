package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.persistence.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.menu.request.MenuCreateRequest;
import kitchenpos.menu.request.MenuProductCreateRequest;
import kitchenpos.product.Product;
import kitchenpos.product.persistence.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuService menuService;

    @Nested
    class 메뉴_생성시 {

        @Test
        void 성공() {
            // given
            var productA = getProduct("치킨", 15000L);
            var productB = getProduct("치즈볼", 5000L);
            var menuGroup = getMenuGroup("세트");
            var menuProducts = List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productB.getId(), 1));
            var request = getMenuCreateRequest("치즈볼 세트", 18000L, menuGroup, menuProducts);

            // when
            Menu actual = menuService.create(request);

            // then
            assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getMenuProducts())
                    .allSatisfy(it -> assertThat(it.getId()).isPositive())
            );
        }

        @ParameterizedTest
        @ValueSource(longs = {-20000L, -1L})
        void 가격이_음수면_예외(Long menuPrice) {
            // given
            var productA = getProduct("치킨", 15000L);
            var productB = getProduct("치즈볼", 5000L);
            var menuGroup = getMenuGroup("세트");
            var menuProducts = List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productB.getId(), 1));
            MenuCreateRequest request = getMenuCreateRequest("치즈볼 세트", menuPrice, menuGroup, menuProducts);

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_아이디의_메뉴그룹이_없으면_예외() {
            // given
            var productA = getProduct("치킨", 15000L);
            var productB = getProduct("치즈볼", 5000L);
            var menuProducts = List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productB.getId(), 1));
            var request = getMenuCreateRequest("치즈볼 세트", 18000L, new MenuGroup(-1L, "없는 메뉴그룹"), menuProducts);

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_상품이_없으면_예외() {
            // given
            var menuGroup = getMenuGroup("세트");
            var menuProducts = List.of(new MenuProductCreateRequest(-1L, 1));
            var request = getMenuCreateRequest("치즈볼 세트", 18000L, menuGroup, menuProducts);

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴상품_가격의_합이_메뉴_가격보다_크면_예외() {
            // given
            var productA = getProduct("치킨", 15000L);
            var productB = getProduct("치즈볼", 5000L);
            var menuGroup = getMenuGroup("세트");
            var menuProducts = List.of(
                new MenuProductCreateRequest(productA.getId(), 1),
                new MenuProductCreateRequest(productB.getId(), 1));
            var request = getMenuCreateRequest("치즈볼 세트", 20001L, menuGroup, menuProducts);

            // when && then
            assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 개별 상품 가격의 합보다 같거나 적어야합니다.");
        }
    }

    @Test
    void 모든_메뉴를_반환() {
        // given
        var productA = getProduct("치킨", 15000L);
        var productB = getProduct("치즈볼", 5000L);
        var productC = getProduct("감튀", 3000L);
        var menuGroupId = getMenuGroup("세트").getId();
        var menuProductsA = List.of(new MenuProduct(productA, 1), new MenuProduct(productB, 1));
        var menuProductsB = List.of(new MenuProduct(productA, 1), new MenuProduct(productC, 1));
        var menuA = menuRepository.save(new Menu("치즈볼 세트", BigDecimal.valueOf(18000L), menuGroupId, menuProductsA));
        var menuB = menuRepository.save(new Menu("감튀 세트", BigDecimal.valueOf(17000L), menuGroupId, menuProductsB));

        // when
        List<Menu> actual = menuService.list();

        // then
        Assertions.assertThat(actual).usingRecursiveComparison()
            .ignoringFields("menuProducts.product")
            .isEqualTo(List.of(menuA, menuB));
    }

    private Product getProduct(String name, long price) {
        return productRepository.save(new Product(name, BigDecimal.valueOf(price)));
    }

    private MenuGroup getMenuGroup(String name) {
        return menuGroupRepository.save(new MenuGroup(name));
    }

    private MenuCreateRequest getMenuCreateRequest(String name, Long price, MenuGroup menuGroup,
                                                   List<MenuProductCreateRequest> menuProducts) {
        return new MenuCreateRequest(name, BigDecimal.valueOf(price), menuGroup.getId(), menuProducts);
    }
}
