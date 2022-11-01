package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        TableGroup tableGroup = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        OrderTable table1 = 주문테이블_생성(new OrderTable(tableGroup.getId(), 5, true));
        OrderTable table2 = 주문테이블_생성(new OrderTable(tableGroup.getId(), 5, true));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = orderTableDao.findAllByIdIn(List.of(table1.getId(), table2.getId()));

        assertThat(tables).extracting("tableGroupId")
                .containsOnlyNulls();
    }

    @DisplayName("테이블의 그룹을 해제할 때 테이블의 주문이 완료 상태가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroupNotCompletion(String status) {
        TableGroup tableGroup = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        OrderTable table1 = 주문테이블_생성(new OrderTable(tableGroup.getId(), 5, true));
        OrderTable table2 = 주문테이블_생성(new OrderTable(tableGroup.getId(), 5, true));

        주문_생성(new Order(table1.getId(), OrderStatus.find(status), LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 완료 상태가 아닙니다.");
    }
}
