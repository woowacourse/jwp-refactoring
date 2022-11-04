package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTables 클래스의")
class OrderTablesTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("그룹화할 orderTable이 2개 미만인 경우 예외를 던진다.")
        void orderTableSize_SmallerThanTwo_ExceptionThrown() {
            // given
            final OrderTable orderTable = new OrderTable(2, false);
            List<OrderTable> orderTables = List.of(orderTable);

            // when & then
            assertThatThrownBy(() -> new OrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("group 메서드는")
    class Group {

        @Test
        @DisplayName("orderTable이 빈 테이블이 아닌 경우 예외를 던진다.")
        void orderTable_NotEmpty_ExceptionThrown() {
            final TableGroup savedTableGroup = new TableGroup(1L, LocalDateTime.now());
            final OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(1L, null, 0, true, Collections.emptyList()),
                    new OrderTable(2L, null, 1, false, Collections.emptyList())
            ));
            assertThatThrownBy(() -> orderTables.group(savedTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 이미 그룹에 속한 경우 예외를 던진다.")
        void orderTable_AlreadyGrouped_ExceptionThrown() {
            final TableGroup savedTableGroup = new TableGroup(1L, LocalDateTime.now());
            final OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(1L, null, 0, true, Collections.emptyList()),
                    new OrderTable(2L, null, 1, false, Collections.emptyList())
            ));
            assertThatThrownBy(() -> orderTables.group(savedTableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
