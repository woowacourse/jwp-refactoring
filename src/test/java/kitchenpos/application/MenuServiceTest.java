package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuServiceTest extends KitchenPosServiceTest {

    @DisplayName("Menu 생성 - 성공")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void create_Success(int value) {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME,
            price.add(BigDecimal.valueOf(value)), getCreatedMenuGroupId(),
            Collections.singletonList(
                new MenuProductRequest(menuProduct.getProduct().getId(),
                    menuProduct.getQuantity())));

        MenuResponse menu = menuService.create(menuRequest);
    }

    @DisplayName("Menu 생성 - 예외 발생, 유효하지 않은 금액")
    @ParameterizedTest
//    @NullSource
    @ValueSource(ints = {-2, -1})
    void create_InvalidPrice_ThrownException(Integer wrongPrice) {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = null;
        if (Objects.nonNull(wrongPrice)) {
            price = BigDecimal.valueOf(wrongPrice);
        }

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, price, getCreatedMenuGroupId(),
            Collections.singletonList(
                new MenuProductRequest(menuProduct.getProduct().getId(),
                    menuProduct.getQuantity())));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, 유효하지 않은 MenuGroupId")
    @ParameterizedTest
    @ValueSource(longs = {-2, -1})
    void create_InvalidMenuGroupId_ThrownException(Long menuGroupId) {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, price, menuGroupId,
            Collections.singletonList(new MenuProductRequest(menuProduct.getProduct().getId(),
                menuProduct.getQuantity())));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, MenuProduct가 Null인 경우")
    @Test
    void create_NullMenuProduct_ThrownException() {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, price,
            getCreatedMenuGroupId(), null);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("Menu 생성 - 예외 발생, Price가 MenuProducts보다 큰 경우")
    @Test
    void create_PriceMoreThanMenuProducts_ThrownException() {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, price.add(BigDecimal.ONE),
            getCreatedMenuGroupId(), Collections.singletonList(
            new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())));

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Menu 조회 - 성공")
    @Test
    void list_Success() {
        MenuProduct menuProduct = getMenuProduct();
        BigDecimal price = getMenuProductPrice(menuProduct);

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, price, getCreatedMenuGroupId(),
            Collections.singletonList(new MenuProductRequest(menuProduct.getProduct().getId(),
                menuProduct.getQuantity())));

        MenuResponse createdMenu = menuService.create(menuRequest);

        List<MenuResponse> menus = menuService.list();
        assertThat(menus).isNotNull();
        assertThat(menus).isNotEmpty();
        assertThat(menus).contains(createdMenu);
    }
}
