package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.exception.InvalidTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    @DisplayName("단체 지정 생성시 테이블 갯수가 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupWithInvalidTableSize() {
        OrderTable orderTable = new OrderTable(null, 10, true);

        assertThatThrownBy(() -> TableGroup.create(LocalDateTime.now(), List.of(orderTable)))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("단체 지정할 테이블은 2개 이상이어야 합니다.");
    }

    @DisplayName("단체 지정 생성시 테이블이 이미 테이블 그룹에 속해있으면 예외가 발생한다.")
    @Test
    void createTableGroupWithInvalidTableStatus1() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        OrderTable orderTable1 = new OrderTable(tableGroup.getId(), 10, true);
        OrderTable orderTable2 = new OrderTable(tableGroup.getId(), 10, true);

        assertThatThrownBy(() -> TableGroup.create(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("테이블 그룹을 지정할 수 없는 테이블입니다.");
    }

    @DisplayName("단체 지정 생성시 테이블이 비어있지 않으면 예외가 발생한다.")
    @Test
    void createTableGroupWithInvalidTableStatus2() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());
        OrderTable orderTable1 = new OrderTable(tableGroup.getId(), 10, false);
        OrderTable orderTable2 = new OrderTable(tableGroup.getId(), 10, false);

        assertThatThrownBy(() -> TableGroup.create(LocalDateTime.now(), List.of(orderTable1, orderTable2)))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("테이블 그룹을 지정할 수 없는 테이블입니다.");
    }
}
