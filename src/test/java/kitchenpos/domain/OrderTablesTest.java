package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTables 클래스의")
class OrderTablesTest {

    @Nested
    @DisplayName("group 메서드는")
    class Group {

        @Test
        @DisplayName("orderTable이 빈 테이블이 아닌 경우 예외를 던진다.")
        void orderTable_NotEmpty_ExceptionThrown() {
            final OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(1L, null, 0, true),
                    new OrderTable(2L, null, 1, false)
            ));
            assertThatThrownBy(() -> orderTables.group())
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("orderTable이 이미 그룹에 속한 경우 예외를 던진다.")
        void orderTable_AlreadyGrouped_ExceptionThrown() {
            OrderTables orderTables = new OrderTables(List.of(
                    new OrderTable(1L, null, 0, true),
                    new OrderTable(2L, null, 1, false)
            ));
            assertThatThrownBy(() -> orderTables.group())
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
