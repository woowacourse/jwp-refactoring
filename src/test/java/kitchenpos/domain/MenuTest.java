package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴_가격이_0보다_작으면_예외가_발생한다() {

        assertThatThrownBy(
                ( )-> new Menu("라면", new BigDecimal(1200), 1L, new ArrayList<>(), new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_가격이_상품들의_가격의_합보다_크면_예외가_발생한다() {
        List<MenuProduct> 메뉴_상품들 = Arrays.asList(
                new MenuProduct(null, 0L, 1),
                new MenuProduct(null, 1L, 1)
        );

        List<Product> 상품들 = Arrays.asList(
                new Product(0L, "계란", new BigDecimal( 800)),
                new Product(1L, "파", new BigDecimal( 300))
        );


        assertThatThrownBy(
                ( )-> {
                    new Menu("라면", new BigDecimal(1200), 1L,
                            메뉴_상품들, 상품들);
                }
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
