package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.dto.TableEmptyRequest;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidTableException;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        OrderTable table = 주문테이블_생성(new OrderTable(null, 5, true));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(empty);

        tableService.changeEmpty(table.getId(), tableEmptyRequest);

        List<OrderTable> tables = tableService.list();
        OrderTable foundTable = tables.stream()
                .filter(t -> table.getId().equals(t.getId()))
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
        TableGroup tableGroup = 단체지정_생성(new TableGroup(LocalDateTime.now()));
        OrderTable table = 주문테이블_생성(new OrderTable(tableGroup.getId(), 5, true));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), tableEmptyRequest))
                .isInstanceOf(InvalidTableException.class)
                .hasMessage("단체 지정 정보가 존재합니다.");
    }

    @DisplayName("테이블의 empty 변경시 주문이 완료 상태가 아니면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithInvalidOrderStatus(String status) {
        OrderTable table = 주문테이블_생성(new OrderTable(null, 5, true));
        주문_생성(new Order(table.getId(), OrderStatus.find(status), LocalDateTime.now()));
        TableEmptyRequest tableEmptyRequest = new TableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), tableEmptyRequest))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("주문이 완료 상태가 아닙니다.");
    }


}
