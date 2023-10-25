package kitchenpos.order.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class NumberOfGuestsTest {

    @Nested
    class 테이블_인원_수_생성_시 {

        @Test
        void 정상적인_테이블_인원_수라면_생성에_성공한다() {
            //given
            int count = 10;

            //when
            NumberOfGuests numberOfGuests = new NumberOfGuests(count);

            //then
            assertThat(numberOfGuests.getNumberOfGuests()).isEqualTo(count);
        }

        @Test
        void 테이블_인원_수가_음수라면_예외를_던진다() {
            //given
            int count = -1;

            //when, then
            assertThatThrownBy(() -> new NumberOfGuests(count))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블 인원 수가 0보다 작을 수 없습니다.");
        }
    }
}
