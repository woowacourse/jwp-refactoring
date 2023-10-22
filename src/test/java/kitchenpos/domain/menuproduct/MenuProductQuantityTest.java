package kitchenpos.domain.menuproduct;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidQuantityException;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuProductQuantityTest {

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void 메뉴_상품_수량이_양수가_아니라면_에러가_발생한다(Long quantity) {
        //given
        //when
        final ThrowingCallable action = () -> new MenuProductQuantity(quantity);

        //then
        assertThatThrownBy(action).isInstanceOf(InvalidQuantityException.class)
                                  .hasMessage("메뉴 상품 수량은 1개 이상이어야 합니다.");
    }
}
