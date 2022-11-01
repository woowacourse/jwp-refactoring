package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.exception.InvalidTableGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @Nested
    @DisplayName("TableGroup을 생성할 때 ")
    class CreateTest {

        @Test
        @DisplayName("OrderTable이 비어있을 경우 예외가 발생한다.")
        void orderTableEmptyFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), Collections.emptyList()))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }

        @Test
        @DisplayName("OrderTable이 기본 수보다 적을 경우 예외가 발생한다.")
        void orderTableSizeFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(new OrderTable(10, true))))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("올바르지 않은 주문 테이블입니다.");
        }


        @Test
        @DisplayName("OrderTable이 이미 사용 중이면 예외가 발생한다.")
        void orderTableAnyUsingFailed() {
            assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(),
                    List.of(new OrderTable(10, false), new OrderTable(10, true))))
                    .isInstanceOf(InvalidTableGroupException.class)
                    .hasMessage("주문 테이블이 이미 사용 중입니다.");
        }
    }

}
