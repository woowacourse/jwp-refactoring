package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable(4, false));

        assertThat(orderTable).isNotNull();
    }

    @DisplayName("테이블들을 조회할 수 있다.")
    @Test
    void list() {
        orderTableDao.save(new OrderTable(0, true));
        orderTableDao.save(new OrderTable(0, true));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("테이블을 빈 상태로 수정할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        OrderTable foundOrderTable = tableService.changeEmpty(orderTable.getId(), new OrderTable(0, true));

        assertThat(foundOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmptyWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeEmpty(9999L, new OrderTable(4, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 상태로 수정 시 테이블이 단체인 경우 예외가 발생한다.")
    @Test
    void changeEmptyWithOrderTableGroup() {
        OrderTable firstOrderTable = orderTableDao.save(new OrderTable(0, true));
        OrderTable secondOrderTable = orderTableDao.save(new OrderTable(0, true));
        List<OrderTable> orderTables = createOrderTable(firstOrderTable, secondOrderTable);
        tableGroupService.create(new TableGroup(orderTables));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable(4, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        OrderTable foundOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(3, false));

        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블의 손님 수 변경 시 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberWithInvalidNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(-1, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeNumberWithNotExistOrderTable() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(9999L, new OrderTable(4, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수 변경 시 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberWithEmptyOrderTable() {
        OrderTable orderTable = tableService.create(new OrderTable(0, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(4, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> createOrderTable(OrderTable... orderTables) {
        return new ArrayList<>(Arrays.asList(orderTables));
    }
}
