package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴가 정상적으로 생성된다.")
    void createMenu() {
        // given
        final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 1);
        final MenuGroup menuGroup = new MenuGroup("치킨");

        // when
        final Menu menu = new Menu(menuGroup.getId(), List.of(menuProduct), "후라이드치킨", new BigDecimal("15000.00"));

        // then
        assertAll(
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(menu.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(menu.getPrice()).isEqualTo("15000.00")
        );
    }

//    @Test
//    @DisplayName("금액의 총합이 메뉴의 가격보다 큰 경우 예외가 발생한다.")
//    void throwsExceptionWhenAmountSumIsLargerThanMenuPrice() {
//        // given
//        final Product product = new Product("후라이드치킨", new BigDecimal("15000.00"));
//        final MenuProduct menuProduct = new MenuProduct(null, product.getId(), 2);
//        final MenuGroup menuGroup = new MenuGroup("치킨");
//
//        // when, then
//        assertThatThrownBy(
//                () -> new Menu(menuGroup.getId(), List.of(menuProduct), "후라이드치킨", new BigDecimal("30001.00")))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
//    }
}
