package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class MenuTest {

//    @Test
//    void 메뉴의_가격이_null이면_예외가_발생한다() {
//        Product product = new Product( "맛있는 라면", new BigDecimal(1300));
//
//        assertThatThrownBy(
//                () -> new Menu("치킨", null, 1L,
//                        Arrays.asList(new MenuProduct(1L, product, 1L)))
//        );
//    }
//
//    @Test
//    void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
//        Product product = new Product("맛있는 라면", new BigDecimal(1300));
//
//        assertThatThrownBy(
//                () -> new Menu("치킨", new BigDecimal(-1), 1L,
//                        Arrays.asList(new MenuProduct(1L, product, 1L)))
//        );
//    }
//
//
//    @Test
//    void 메뉴_그룹_존재하지_않으면_예외가_발생한다() {
//        Product product = new Product("맛있는 라면", new BigDecimal(1300));
//
//        assertThatThrownBy(
//                () -> new Menu("피자", new BigDecimal(1000), 0L,
//                        Arrays.asList(new MenuProduct(1L, product, 1L)))
//        );
//    }
//
//    @Test
//    void 존재하지_않는_상품을_사용하면_예외가_발생한다() {
//        Product product = new Product("맛있는 라면", new BigDecimal(1300));
//
//        assertThatThrownBy(
//                () -> new Menu("피자", new BigDecimal(1000), 1L,
//                        Arrays.asList(new MenuProduct(1L, product, 1L)))
//        );
//    }
//
//    @Test
//    void 메뉴_가격이_상품들의_가격의_합보다_크면_예외가_발생한다() {
//        Product product = new Product("맛있는 라면", new BigDecimal(1300));
//
//        assertThatThrownBy(
//                () -> new Menu("치킨", new BigDecimal(2000), 1L,
//                        Arrays.asList(new MenuProduct(1L, product, 1L)))
//        );
//    }
}
