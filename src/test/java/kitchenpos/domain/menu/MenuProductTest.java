package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.domain.DomainTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품을 생성한다.")
    void createMenuProduct() {
        final Menu testMenu = DomainTestFixture.getTestMenu();
        final MenuProduct menuProduct = new MenuProduct(
                testMenu,
                "상품이름",
                BigDecimal.valueOf(1000L),
                2L,
                10
        );

        assertAll(
                () -> assertThat(menuProduct.getMenuId()).isEqualTo(testMenu.getId()),
                () -> assertThat(menuProduct.getProductName()).isEqualTo("상품이름"),
                () -> assertThat(menuProduct.getProductPrice().getValue().longValue()).isEqualTo(1000L),
                () -> assertThat(menuProduct.getProductId()).isEqualTo(2L),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(10)
        );
    }
}