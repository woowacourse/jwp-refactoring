package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.request.TableEmptyRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.exception.InvalidTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ApplicationTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        주문테이블_생성(new OrderTable(null, 0, true));
        주문테이블_생성(new OrderTable(null, 0, true));

        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(2);
    }

    @DisplayName("테이블의 empty를 수정한다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void changeEmpty(boolean empty) {
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, true));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(empty);

        tableService.changeEmpty(tableId, tableEmptyRequest);

        List<OrderTable> tables = tableService.list();
        OrderTable foundTable = tables.stream()
                .filter(t -> tableId.equals(t.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(foundTable).extracting("empty").isEqualTo(empty);
    }

    @DisplayName("테이블의 empty 변경시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmptyWithNotFoundTable() {
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1000L, tableEmptyRequest))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("테이블이 존재하지 않습니다.");
    }

    @DisplayName("테이블의 empty 변경시 단체 지정 정보가 존재하면 예외가 발생한다.")
    @Test
    void changeEmptyWithTableGroup() {
        Long tableGroupId = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        Long tableId = 주문테이블_생성(new OrderTable(tableGroupId, 5, true));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(tableId, tableEmptyRequest))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("단체 지정 정보가 존재합니다.");
    }

    @DisplayName("테이블의 empty 변경시 주문이 완료 상태가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithInvalidOrderStatus(String status) {
        Long tableId = 주문테이블_생성(new OrderTable(null, 5, true));
        주문_생성(new Order(tableId, OrderStatus.find(status), LocalDateTime.now()));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(tableId, tableEmptyRequest))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 완료 상태가 아닙니다.");
    }


}
