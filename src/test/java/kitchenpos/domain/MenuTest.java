package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidMenuException;
import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Nested
    class 메뉴_생성시 {
        @Test
        void 가격이_null이면_예외가_발생한다() {
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1));
            assertThatThrownBy(() -> new Menu("양념치킨세트", null, 1L, menuProducts))
                    .isInstanceOf(InvalidPriceException.class);
        }

        @Test
        void 가격이_음수이면_예외가_발생한다() {
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1));
            assertThatThrownBy(() -> new Menu("양념치킨세트", BigDecimal.valueOf(-1000), 1L, menuProducts))
                    .isInstanceOf(InvalidPriceException.class);
        }

        @Test
        void 포함된_메뉴_그룹이_없으면_예외가_발생한다() {
            final List<MenuProduct> menuProducts = List.of(new MenuProduct(1L, 1L, 1));
            assertThatThrownBy(() -> new Menu("양념치킨세트", BigDecimal.valueOf(1000), null, menuProducts))
                    .isInstanceOf(InvalidMenuException.class);
        }

        @Test
        void 포함된_상품이_없으면_예외가_발생한다() {
            assertThatThrownBy(() -> new Menu("양념치킨세트", BigDecimal.valueOf(1000), 1L, Collections.emptyList()))
                    .isInstanceOf(InvalidMenuException.class);
        }
    }
}
