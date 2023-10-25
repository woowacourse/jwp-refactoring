package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.InvalidPriceException;
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
        final ThrowingSupplier<Menu> action = () -> new Menu("메뉴이름", price, menuProducts, new MenuGroup("메뉴 그룹 이름"));

        //then
        assertDoesNotThrow(action);
    }

    @Test
    void 메뉴_상품_가격이_합보다_메뉴_가격이_크다면_에러가_발생한다() {
        //given
        final var price = BigDecimal.valueOf(1100);
        final var menuProducts = List.of(new MenuProduct(new Product("상품 이름", BigDecimal.valueOf(1000)), 1L));

        //when
        final ThrowingCallable action = () -> new Menu("메뉴이름", price, menuProducts, new MenuGroup("메뉴 그룹 이름"));

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidPriceException.class)
                                    .hasMessage("메뉴의 가격은 메뉴 상품의 가격 합보다 작거나 같아야 합니다.");
    }
}
