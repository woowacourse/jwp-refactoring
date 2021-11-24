package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class MenuTest {

    @DisplayName("메뉴보다 단품 가격이 비싸면 예외가 발생한다.")
    @Test
    void validatePrice() {
        Menu menu = new Menu("메뉴", 1000L, null, null);

        assertThatThrownBy(() -> menu.validatePrice(new BigDecimal(999)))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴에 등록된 상품을 변경한다.")
    @Test
    void changeMenuProduct() {
        Product product = new Product(1L, "샘플", new BigDecimal(1000L));
        Product product2 = new Product(2L, "샘플2", new BigDecimal(1500L));
        List<MenuProduct> menuProducts = Arrays.asList(
            new MenuProduct(1L, null, product, 1),
            new MenuProduct(2L, null, product2, 3)
        );
        Menu menu = new Menu("메뉴", 1000L, null, null);
        Menu expected = new Menu("메뉴", 1000L, null, Arrays.asList(
            new MenuProduct(1L, null, product, 1),
            new MenuProduct(2L, null, product2, 3)
        ));

        menu.changeMenuProduct(menuProducts);

        assertThat(menu).usingRecursiveComparison().isEqualTo(expected);
    }
}
