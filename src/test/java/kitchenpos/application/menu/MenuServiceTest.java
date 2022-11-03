package kitchenpos.application.menu;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.MenuGroupFactory.createMenuGroup;
import static kitchenpos.fixture.MenuRequestBuilder.aMenuRequest;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuService sut;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("Menu의 가격은 null일 수 없다")
    void throwException_WhenPriceNull() {
        // given
        var menu = aMenuRequest(savedMenuGroup().getId())
                .withPrice(null)
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다");
    }

    @Test
    @DisplayName("Menu의 가격은 음수일 수 없다")
    void throwException_WhenPriceNegative() {
        // given
        var menu = aMenuRequest(savedMenuGroup().getId())
                .withPrice(BigDecimal.valueOf(-1L))
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다");
    }

    @Test
    @DisplayName("Menu의 MenuGroupId가 존재하지 않으면 Menu를 생성할 수 없다")
    void throwException_WhenGivenNonExistMenuGroupId() {
        // given
        final var NON_EXIST_ID = 0L;
        var menu = aMenuRequest(NON_EXIST_ID)
                .build();

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 그룹이 존재하지 않습니다");
    }

    @Test
    @DisplayName("Menu에 포함된 Product가 존재하지 않으면 Menu를 생성할 수 없다")
    void throwException_WhenGivenNonExistMenuProductId() {
        // given
        final var NON_EXIST_ID = 0L;
        var menu = aMenuRequest(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProductRequest(NON_EXIST_ID, 1)))
                .build();

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품이 존재하지 않습니다");
    }

    @Test
    @DisplayName("Product의 가격의 합보다 Menu의 가격이 더 커서는 안된다")
    void throwException_WhenMenuPriceAndSumOfMenuProductPrice_NotMatch() {
        // given
        final var PRODUCT_PRICE = BigDecimal.valueOf(1000L);
        final var QUANTITY = 2;
        final var WRONG_MENU_PRICE = PRODUCT_PRICE.multiply(BigDecimal.valueOf(QUANTITY))
                .add(BigDecimal.valueOf(1000L));

        var productId = productRepository.save(
                aProduct()
                        .withPrice(PRODUCT_PRICE)
                        .build()
        ).getId();

        var menu = aMenuRequest(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProductRequest(productId, QUANTITY)))
                .withPrice(WRONG_MENU_PRICE)
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 상품 전체 가격의 합보다 클 수 없습니다");
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void saveMenu() {
        // given
        final var PRODUCT_PRICE = BigDecimal.valueOf(14_000L);
        final var QUANTITY = 1L;
        final var MENU_PRICE = PRODUCT_PRICE.multiply(BigDecimal.valueOf(QUANTITY));

        Long productId = productRepository.save(
                aProduct()
                        .withPrice(PRODUCT_PRICE)
                        .build()
        ).getId();

        var menu = aMenuRequest(savedMenuGroup().getId())
                .withPrice(MENU_PRICE)
                .withMenuProducts(List.of(new MenuProductRequest(productId, QUANTITY)))
                .build();

        // when
        var savedMenu = sut.create(menu);

        // then
        List<MenuProductResponse> menuProducts = savedMenu.getMenuProducts();
        assertThat(savedMenu).isNotNull();
        assertThat(menuProducts).hasSize(1);
        for (var menuProduct : menuProducts) {
            assertThat(menuProduct.getMenuId()).isEqualTo(savedMenu.getId());
        }
    }

    @Test
    @DisplayName("Menu 목록을 조회한다")
    void listMenus() {
        var product = productRepository.save(aProduct().build());
        menuRepository.save(aMenu(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProduct(product.getId(), 1L, product.getPrice())))
                .build());

        var menus = sut.list();

        assertThat(menus).hasSize(1);
    }

    private MenuGroup savedMenuGroup() {
        return menuGroupRepository.save(createMenuGroup());
    }
}
