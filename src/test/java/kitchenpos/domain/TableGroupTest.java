package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @DisplayName("두개 이상의 주문테이블이 없는 경우 예외가 발생한다.")
    @Test
    void createTableGroupWithUnderTwoOrderTable() {
        OrderTable orderTable = new OrderTable(100, true);

        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTable)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 주문테이블이 있는 경우 예외가 발생한다.")
    @Test
    void createTableGroupWithEmptyOrderTable() {
        OrderTable orderTableA = new OrderTable(100, false);
        OrderTable orderTableB = new OrderTable(100, true);

        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTableA, orderTableB)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체가 이미 지정된 경우 예외가 발생한다.")
    @Test
    void createTableGroupWithOrderTableWhichAlreadyHasGroup() {
        OrderTable orderTableA = new OrderTable(100, true);
        orderTableA.enrollTableGroup(new TableGroup());
        OrderTable orderTableB = new OrderTable(100, true);

        assertThatThrownBy(() -> new TableGroup(LocalDateTime.now(), List.of(orderTableA, orderTableB)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정상적으로 생성된 경우를 테스트한다.")
    @Test
    void createTableGroup() {
        OrderTable orderTableA = new OrderTable(100, true);
        OrderTable orderTableB = new OrderTable(100, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTableA, orderTableB));

        assertThat(orderTableA.getTableGroup()).isSameAs(tableGroup);
    }
}
