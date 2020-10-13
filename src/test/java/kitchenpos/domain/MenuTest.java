package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("[예외] 가격이 0보다 작은 메뉴 생성")
    @Test
    void create_Fail_With_InvalidPrice() {
        assertThatThrownBy(
            () -> Menu.builder()
                .price(BigDecimal.valueOf(-1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 포함된 상품 가격 총합 계산")
    @Test
    void calculateProductPrice() {
        Product product1 = createProduct(15_000);
        Product product2 = createProduct(16_000);

        Menu menu = createMenu(30_000);

        createMenuProduct(menu, product1, 1);
        createMenuProduct(menu, product2, 1);

        BigDecimal actual = menu.calculateProductPrice();
        BigDecimal expected = BigDecimal.valueOf(31_000);
        assertThat(actual).isEqualTo(expected);
    }
}