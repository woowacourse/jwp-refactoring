package kitchenpos.domain.menu;

import static kitchenpos.domain.DomainTestFixture.testMenu;
import static kitchenpos.domain.DomainTestFixture.testProduct1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품을 생성한다.")
    void createMenuProduct() {
        final MenuProduct menuProduct = new MenuProduct(testMenu.getId(), testProduct1.getId(), 10);

        assertAll(
                () -> assertThat(menuProduct.getMenuId()).isEqualTo(testMenu.getId()),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(testProduct1.getId()),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(10)
        );
    }
}