package kitchenpos.ui;

import static fixture.MenuGroupFixtures.두마리메뉴_그룹;
import static fixture.MenuGroupFixtures.존재하지않는_그룹_ID;
import static fixture.ProductFixtures.존재하지_않는_상품_ID;
import static fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.IntegrationTest;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class MenuRestControllerTest {

    @Autowired
    private MenuRestController sut;

    @DisplayName("메뉴의 가격은 0원 이상이어야 한다.")
    @Test
    void priceMustOverZero() {
        // arrange
        MenuProduct menuProduct = createMenuProduct(후라이드_상품.id(), 2);
        Menu menu = createMenu("후라이드+후라이드", BigDecimal.ONE.negate(), 두마리메뉴_그룹.id(), menuProduct);

        // act & assert
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 null이 아니어야 한다.")
    @Test
    void priceMustNotNull() {
        // arrange
        MenuProduct menuProduct = createMenuProduct(후라이드_상품.id(), 2);
        Menu menu = createMenu("후라이드+후라이드", null, 두마리메뉴_그룹.id(), menuProduct);

        // act & assert
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹은 존재해야 한다.")
    @Test
    void menuGroupMustExist() {
        // arrange
        MenuProduct menuProduct = createMenuProduct(후라이드_상품.id(), 2);
        Menu menu = createMenu("후라이드+후라이드", BigDecimal.ZERO, 존재하지않는_그룹_ID(), menuProduct);

        // act & assert
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품은 존재해야 한다.")
    @Test
    void productMustExist() {
        // arrange
        MenuProduct menuProduct = createMenuProduct(존재하지_않는_상품_ID(), 1);
        Menu menu = createMenu("후라이드+후라이드", BigDecimal.ZERO, 두마리메뉴_그룹.id(), menuProduct);

        // act & assert
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 상품의 총합보다 클 수 없다.")
    @Test
    void priceMustUnderTotalOfMenuProductPrice() {
        // arrange
        MenuProduct menuProduct = createMenuProduct(후라이드_상품.id(), 1);
        BigDecimal overPrice = BigDecimal.valueOf(후라이드_상품.가격() + 1);
        Menu menu = createMenu("후라이드+후라이드", overPrice, 두마리메뉴_그룹.id(), menuProduct);

        // act & assert
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenu(String name, BigDecimal price, long menuGroupId,
                            MenuProduct... menuProduct) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setMenuGroupId(menuGroupId);
        menu.setPrice(price);
        menu.setMenuProducts(List.of(menuProduct));
        return menu;
    }

    private MenuProduct createMenuProduct(long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

}
