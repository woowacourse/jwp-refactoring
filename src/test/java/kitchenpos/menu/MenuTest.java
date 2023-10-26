package kitchenpos.menu;

import kitchenpos.product.Product;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void 메뉴_가격이_0보다_작으면_예외가_발생한다(int price) {
        assertThatThrownBy(() -> new Menu("메뉴", BigDecimal.valueOf(price), 1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ValueSource(ints = {0, 1})
    @ParameterizedTest
    void 메뉴_가격이_0보다_크거나_같으면_정상_생성된다(int price) {
        Product product = new Product("name", BigDecimal.valueOf(1000));
        MenuProduct menuProduct = new MenuProduct(1L, 1L);
        assertThatCode(() -> new Menu("메뉴", BigDecimal.valueOf(price), 1L, List.of(menuProduct)))
                .doesNotThrowAnyException();
    }
}
