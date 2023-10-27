package kitchenpos.application;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.application.request.MenuCreateRequest;
import kitchenpos.menu.application.request.MenuProductCreateRequest;
import kitchenpos.menu.application.response.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private static Stream<BigDecimal> menuPriceSource() {
        return Stream.of(
                new BigDecimal(-3),
                null
        );
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.MENU_GROUP1);
        Product product = productRepository.save(ProductFixture.PRODUCT_1);

        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(product.getId(), 3);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("치킨메뉴", new BigDecimal("48000.00"), menuGroup.getId(),
                List.of(menuProductCreateRequest));

        MenuProduct menuProduct = new MenuProduct(product, 3);
        Menu menu = Menu.of("치킨메뉴", new BigDecimal("48000.00"), menuGroup,
                new MenuProducts(List.of(menuProduct)));

        // when
        MenuResponse createdMenu = menuService.create(menuCreateRequest);

        // then
        assertThat(createdMenu).usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.menu", "menuProducts.seq")
                .isEqualTo(menu);
    }

    @ParameterizedTest
    @MethodSource("menuPriceSource")
    void 메뉴_가격이_음수거나_null이면_예외_발생한다(BigDecimal price) {
        // given
        MenuCreateRequest menu = new MenuCreateRequest("한식", price, 1L,
                List.of(new MenuProductCreateRequest(3L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_상품들의_가격의_합이_메뉴_가격보다_크면_예외_발생한다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.MENU_GROUP1);
        Product product = productRepository.save(ProductFixture.PRODUCT_1);

        MenuCreateRequest menu =
                new MenuCreateRequest("치킨메뉴", new BigDecimal("33000.00"),
                        menuGroup.getId(), List.of(new MenuProductCreateRequest(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_리스트를_조회한다() {
        // given, when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).usingRecursiveComparison()
                .ignoringFields("id", "menuGroup.id", "menuProducts.seq", "menuProducts.menu", "menuProducts.product.id")
                .isEqualTo(List.of(MenuFixture.MENU_1, MenuFixture.MENU_2, MenuFixture.MENU_3, MenuFixture.MENU_4, MenuFixture.MENU_5, MenuFixture.MENU_6));
    }
}
