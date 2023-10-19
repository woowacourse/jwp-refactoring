package kitchenpos.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {


    @Test
    void 주문_테이블_비어있는_정보_수정() {
        // given
        OrderTable orderTable = OrderTable.builder()
                .empty(true)
                .build();
        // when & then
        OrderTable updated = orderTable.updateEmpty(false);
        assertThat(updated.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블_비어있는_정보_수정_시_테이블_그룹이_Null이_아니면_예외() {
        // given
        OrderTable orderTable = OrderTable.builder()
                .tableGroupId(1L)
                .empty(true)
                .build();

        // when & then
        assertThatThrownBy(() -> orderTable.updateEmpty(false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_손님_수_수정() {
        // given
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(1)
                .build();
        // when & then
        assertDoesNotThrow(() -> orderTable.updateNumberOfGuests(2));
    }

    @Test
    void 주문_테이블_손님_수_수정_시_손님_수가_0보다_작으면_예외() {
        // given
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(0)
                .build();

        // when & then
        assertThatThrownBy(() -> orderTable.updateNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
