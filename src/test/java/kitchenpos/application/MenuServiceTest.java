package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_PRICE;
import static kitchenpos.constants.Constants.TEST_MENU_PRODUCT_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends KitchenPosServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("Menu 생성 - 성공")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void create_Success(int value) {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(price.add(BigDecimal.valueOf(value)));
        menu.setMenuGroupId(getCreatedMenuGroupId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        menuService.create(menu);
    }

    @DisplayName("Menu 생성 - 예외 발생, 유효하지 않은 금액")
    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-2, -1})
    void create_InvalidPrice_ThrownException(Integer price) {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        if (Objects.nonNull(price)) {
            menu.setPrice(BigDecimal.valueOf(price));
        }
        menu.setMenuGroupId(getCreatedMenuGroupId());
        menu.setMenuProducts(Collections.singletonList(getMenuProduct()));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, 유효하지 않은 MenuGroupId")
    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {-2, -1})
    void create_InvalidMenuGroupId_ThrownException(Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Collections.singletonList(getMenuProduct()));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, MenuProduct가 Null인 경우")
    @Test
    void create_NullMenuProduct_ThrownException() {
        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(TEST_MENU_PRICE);
        menu.setMenuGroupId(getCreatedMenuGroupId());

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, Price가 MenuProducts보다 큰 경우")
    @Test
    void create_PriceMoreThanMenuProducts_ThrownException() {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(price.add(BigDecimal.ONE));
        menu.setMenuGroupId(getCreatedMenuGroupId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Menu 조회 - 성공")
    @Test
    void list_Success() {
        MenuProduct menuProduct = getMenuProduct();

        Menu menu = new Menu();
        menu.setName(TEST_MENU_NAME);
        menu.setPrice(getMenuProductPrice(menuProduct));
        menu.setMenuGroupId(getCreatedMenuGroupId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));
        Menu createdMenu = menuService.create(menu);

        List<Menu> menus = menuService.list();
        assertThat(menus).isNotNull();
        assertThat(menus).isNotEmpty();

        List<Long> menuIds = menus.stream()
            .map(Menu::getId)
            .collect(Collectors.toList());
        assertThat(menuIds).contains(createdMenu.getId());
    }

    private MenuProduct getMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(TEST_MENU_PRODUCT_QUANTITY);
        menuProduct.setProductId(getCreatedProductId());
        return menuProduct;
    }

    private BigDecimal getMenuProductPrice(MenuProduct menuProduct) {
        BigDecimal productPrice = productDao.findById(menuProduct.getProductId())
            .orElseThrow(() -> new IllegalArgumentException(
                menuProduct.getMenuId() + "ID에 해당하는 Product가 없습니다."))
            .getPrice();
        BigDecimal quantity = BigDecimal.valueOf(menuProduct.getQuantity());
        return productPrice.multiply(quantity);
    }
}
