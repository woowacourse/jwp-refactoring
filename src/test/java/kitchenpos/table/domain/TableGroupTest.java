package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @DisplayName("테이블 그룹을 생성하는 기능")
    @Nested
    class CreateTest {

        @DisplayName("테이블이 empty 상태가 아니면 예외가 발생한다.")
        @Test
        void create_Exception_NotEmptyStatus() {
            final OrderTable orderTable = OrderTable.createWithoutTableGroup(0, false);

            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블의 테이블 그룹이 이미 존재한다면 예외가 발생한다.")
        @Test
        void create_Exception_AlreadyExist() {
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                    List.of(new OrderTable(null, 0, true),
                            new OrderTable(null, 0, true)));
            final OrderTable orderTable1 = new OrderTable(tableGroup, 0, true);
            final OrderTable orderTable2 = new OrderTable(tableGroup, 0, true);

            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
