package kitchenpos.menu.ui;

import static fixture.MenuGroupFixtures.두마리메뉴_그룹;
import static fixture.MenuGroupFixtures.존재하지않는_그룹_ID;
import static fixture.ProductFixtures.존재하지_않는_상품_ID;
import static fixture.ProductFixtures.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import common.IntegrationTest;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
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
        MenuProductRequest menuProductRequest = createMenuProductRequest(후라이드_상품.id(), 2);
        MenuRequest menuRequest = createMenuRequest("후라이드+후라이드", BigDecimal.ONE.negate(), 두마리메뉴_그룹.id(),
                menuProductRequest);

        // act & assert
        assertThatThrownBy(() -> sut.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 null이 아니어야 한다.")
    @Test
    void priceMustNotNull() {
        // arrange
        MenuProductRequest menuProductRequest = createMenuProductRequest(후라이드_상품.id(), 2);
        MenuRequest menuRequest = createMenuRequest("후라이드+후라이드", null, 두마리메뉴_그룹.id(), menuProductRequest);

        // act & assert
        assertThatThrownBy(() -> sut.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹은 존재해야 한다.")
    @Test
    void menuGroupMustExist() {
        // arrange
        MenuProductRequest menuProductRequest = createMenuProductRequest(후라이드_상품.id(), 2);
        MenuRequest menuRequest = createMenuRequest("후라이드+후라이드", BigDecimal.ZERO, 존재하지않는_그룹_ID(),
                menuProductRequest);

        // act & assert
        assertThatThrownBy(() -> sut.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품은 존재해야 한다.")
    @Test
    void productMustExist() {
        // arrange
        MenuProductRequest menuProductRequest = createMenuProductRequest(존재하지_않는_상품_ID(), 1);
        MenuRequest menuRequest = createMenuRequest("후라이드+후라이드", BigDecimal.ZERO, 두마리메뉴_그룹.id(),
                menuProductRequest);

        // act & assert
        assertThatThrownBy(() -> sut.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격은 상품의 총합보다 클 수 없다.")
    @Test
    void priceMustUnderTotalOfMenuProductPrice() {
        // arrange
        MenuProductRequest menuProductRequest = createMenuProductRequest(후라이드_상품.id(), 1);
        BigDecimal overPrice = BigDecimal.valueOf(후라이드_상품.가격() + 1);
        MenuRequest menuRequest = createMenuRequest("후라이드+후라이드", overPrice, 두마리메뉴_그룹.id(), menuProductRequest);

        // act & assert
        assertThatThrownBy(() -> sut.create(menuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuRequest createMenuRequest(String name, BigDecimal price, long menuGroupId,
                                          MenuProductRequest... menuProduct) {
        return new MenuRequest(name, price, menuGroupId, List.of(menuProduct));
    }

    private MenuProductRequest createMenuProductRequest(long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
