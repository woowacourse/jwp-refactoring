package kitchenpos.domain;

import java.util.Map;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴_상품들_가격_합보다_크면_예외가_발생한다() {
        //given
        Map<Product, Integer> 메뉴_상품들 =
                Map.of(new Product(1L, "상품", Money.of(10_000)), 2,
                        new Product(2L, "상품", Money.of(3_000)), 5
                );

        int 가격_합 = 메뉴_상품들.entrySet()
                .stream().mapToInt(entry -> entry.getKey().getPrice().getValue().intValue() * entry.getValue())
                .sum();

        Money 합보다_큰_가격 = Money.of(가격_합 + 1);
        Menu 메뉴 = Menu.of( "메뉴", 합보다_큰_가격, null, Map.of(new Product("상품", Money.of(30_000)), 2));

        //expect
        assertThatThrownBy(() -> 메뉴.changeMenuProducts(메뉴_상품들))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_null이면_예외가_발생한다() {
        //given
        final Money 가격 = null;

        //expect
        assertThatThrownBy(() -> Menu.of("메뉴", 가격, 1L, Map.of(new Product("상품", Money.of(10_000)), 1)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void id가_같으면_동등하다() {
        //given
        Menu 메뉴 = new Menu(1L, "메뉴", Money.of(10_000), 1L);

        //when
        boolean actual = 메뉴.equals(new Menu(1L, "다른메뉴", Money.of(10_000), 1L));

        //then
        assertThat(actual).isTrue();
    }

}
