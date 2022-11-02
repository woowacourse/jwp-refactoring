package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ApplicationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블의 그룹을 해제한다.")
    @Test
    void ungroup() {
        Long tableGroupId = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        Long tableId1 = 주문테이블_생성(new OrderTable(tableGroupId, 5, true));
        Long tableId2 = 주문테이블_생성(new OrderTable(tableGroupId, 5, true));

        tableGroupService.ungroup(tableGroupId);

        List<OrderTable> tables = orderTableDao.findAllByIdIn(List.of(tableId1, tableId2));

        assertThat(tables).extracting("tableGroupId")
                .containsOnlyNulls();
    }

    @DisplayName("테이블의 그룹을 해제할 때 테이블의 주문이 완료 상태가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupNotCompletion(String status) {
        Long tableGroupId = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        Long tableId1 = 주문테이블_생성(new OrderTable(tableGroupId, 5, true));
        Long tableId2 = 주문테이블_생성(new OrderTable(tableGroupId, 5, true));

        주문_생성(new Order(tableId1, OrderStatus.find(status), LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 완료 상태가 아닙니다.");
    }
}
