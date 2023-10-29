package kitchenpos.domain;

import java.util.List;
import kitchenpos.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class MenuTest {

    @Test
    void id가_같으면_동등하다() {
        //given
        Menu 메뉴 = new Menu(1L, "메뉴", Money.of(10_000), 1L, List.of(mock(MenuProduct.class), mock(MenuProduct.class)));

        //when
        boolean actual = 메뉴.equals(
                new Menu(1L, "다른메뉴", Money.of(10_000), 1L, List.of(mock(MenuProduct.class), mock(MenuProduct.class))));

        //then
        assertThat(actual).isTrue();
    }

    @Nested
    class 메뉴를_생성할_때 {

        @Test
        void 가격이_null이면_예외가_발생한다() {
            //given
            final Money 가격 = null;

            //expect
            assertThatThrownBy(
                    () -> new Menu(null, "메뉴", 가격, 1L, List.of(mock(MenuProduct.class), mock(MenuProduct.class))))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void 리스트가_비어있으면_예외가_발생한다() {
            //given
            List<MenuProduct> 메뉴상품_목록 = List.of();

            //expect
            assertThatThrownBy(() -> new Menu(null, "메뉴", Money.of(10_000), 1L, 메뉴상품_목록))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 리스트가_null이면_예외가_발생한다() {
            //given
            List<MenuProduct> 메뉴상품_목록 = null;

            //expect
            assertThatThrownBy(() -> new Menu(null, "메뉴", Money.of(10_000), 1L, 메뉴상품_목록))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴그룹아이디가_null이면_예외가_발생한다() {
            //given
            Long 메뉴그룹아이디 = null;

            //expect
            assertThatThrownBy(() -> new Menu(null, "메뉴", Money.of(10_000), 메뉴그룹아이디,
                    List.of(mock(MenuProduct.class), mock(MenuProduct.class))))
                    .isInstanceOf(NullPointerException.class);
        }

    }

}
