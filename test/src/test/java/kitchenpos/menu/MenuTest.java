package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import exception.InvalidPriceException;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    void 메뉴를_정상_생성한다() {
        //given
        final var price = BigDecimal.valueOf(1000);
        final var menuProducts = List.of(new MenuProduct(new Product("상품 이름", BigDecimal.valueOf(1000)), 1L));

        //when
        final ThrowingSupplier<Menu> action = () -> new Menu("메뉴이름", price, menuProducts, new MenuGroup("메뉴 그룹 이름").getId());

        //then
        assertDoesNotThrow(action);
    }

    @Test
    void 메뉴_상품_가격이_합보다_메뉴_가격이_크다면_에러가_발생한다() {
        //given
        final var price = BigDecimal.valueOf(1100);
        final var menuProducts = List.of(new MenuProduct(new Product("상품 이름", BigDecimal.valueOf(1000)), 1L));

        //when
        final ThrowingCallable action = () -> new Menu("메뉴이름", price, menuProducts, new MenuGroup("메뉴 그룹 이름").getId());

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidPriceException.class)
                                    .hasMessage("메뉴의 가격은 메뉴 상품의 가격 합보다 작거나 같아야 합니다.");
    }
}
