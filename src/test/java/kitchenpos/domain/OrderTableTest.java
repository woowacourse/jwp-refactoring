package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.exception.KitchenPosException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Nested
    class 생성 {

        @Test
        void 방문한_손님_수가_음수이면_예외() {
            // given
            int numberOfGuests = -1;

            // when & then
            assertThatThrownBy(() -> new OrderTable(1L, false, numberOfGuests))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("방문한 손님 수는 음수일 수 없습니다.");
        }

        @Test
        void 성공() {
            // when
            OrderTable orderTable = new OrderTable(1L, false, 0);

            // then
            assertThat(orderTable.getId()).isEqualTo(1L);
            assertThat(orderTable.findTableGroupId()).isNull();
        }
    }

    @Nested
    class changeTableGroup {

        @Test
        void 테이블의_상태가_비어있으면_예외() {
            OrderTable orderTable = new OrderTable(1L, true, 0);
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            TableGroup tableGroup = new TableGroup(1L, createdDate);

            // when & then
            assertThatThrownBy(() -> orderTable.changeTableGroup(tableGroup))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("비어있는 상태의 주문 테이블은 테이블 그룹에 등록할 수 없습니다.");
        }

        @Test
        void 이미_테이블_그룹에_속해있으면_예외() {
            OrderTable orderTable = new OrderTable(1L, false, 0);
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            TableGroup tableGroup = new TableGroup(1L, createdDate);
            orderTable.changeTableGroup(tableGroup);


            // when & then
            assertThatThrownBy(() -> orderTable.changeTableGroup(tableGroup))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("이미 테이블 그룹에 속해있는 주문 테이블 입니다.");
        }
    }

    @Nested
    class changeEmpty {

        @Test
        void tableGroupId가_null이_아니면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            TableGroup tableGroup = new TableGroup(1L, createdDate);
            orderTable.changeTableGroup(tableGroup);

            // when & then
            assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("테이블 그룹에 속한 테이블은 상태를 변경할 수 없습니다.");
        }

        @Test
        void tableGroupId가_null이면_성공() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);

            // when
            orderTable.changeEmpty(true);

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }
    }
}
