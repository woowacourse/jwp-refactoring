package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.DomainTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품을 생성한다.")
    void createMenuProduct() {
        final Menu testMenu = DomainTestFixture.getTestMenu();
        final MenuProduct menuProduct = new MenuProduct(testMenu, 2L, 10);

        assertAll(
                () -> assertThat(menuProduct.getMenuId()).isEqualTo(testMenu.getId()),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(2L),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(10)
        );
    }
}