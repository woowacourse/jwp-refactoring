package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("단일 메뉴를 생성한다")
    @Test
    void create() {
        // given
        final int newMenuId = menuService.list().size() + 1;
        final Menu menu = createMenu(1L, BigDecimal.valueOf(29), List.of(1L, 2L));

        // when
        final Menu actual = menuService.create(menu);

        // then
        assertThat(actual.getId()).isEqualTo(newMenuId);
    }

    @DisplayName("메뉴 생성에 실패한다")
    @ParameterizedTest(name = "{0} 메뉴 생성 시 실패한다")
    @MethodSource("menuParameterProvider")
    void create_Fail(
            final String testName,
            final Long menuGroupId,
            final BigDecimal price,
            final List<Long> products,
            final Class exception,
            final String message
    ) {
        // given
        final Menu menu = createMenu(menuGroupId, price, products);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(exception)
                .hasMessage(message);
    }

    private static Stream<Arguments> menuParameterProvider() {
        return Stream.of(
                Arguments.of("음수의 가격을 가진", 1L, BigDecimal.valueOf(-1), List.of(1L, 2L),
                        IllegalArgumentException.class, null),
                Arguments.of("메뉴 그룹에 속하지 않은", -1L, BigDecimal.valueOf(100), List.of(1L, 2L),
                        IllegalArgumentException.class, null),
                Arguments.of("없는 상품을 가진", 1L, BigDecimal.valueOf(100), List.of(-1L),
                        IllegalArgumentException.class, null),
                Arguments.of("없는 상품을 가진", 1L, BigDecimal.valueOf(100000000), List.of(1L),
                        IllegalArgumentException.class, null)
        );
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void list() {
        // given & when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    private Menu createMenu(
            final Long menuGroupId,
            final BigDecimal price,
            final List<Long> products
    ) {
        final Menu menu = new Menu();
        menu.setPrice(price);
        menu.setMenuProducts(createMenuProductList(products));
        menu.setMenuGroupId(menuGroupId);
        menu.setName("test");

        return menu;
    }

    private List<MenuProduct> createMenuProductList(final List<Long> products) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (Long product : products) {
            menuProducts.add(setMenuProduct(product));
        }
        return menuProducts;
    }

    private MenuProduct setMenuProduct(final Long value) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(value);
        menuProduct.setProductId(value);
        menuProduct.setQuantity(value);
        menuProduct.setSeq(value);
        return menuProduct;
    }
}
