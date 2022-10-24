package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;

class TableServiceTest extends ServiceTest {

    @Autowired
    protected TableService tableService;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected OrderTableDao orderTableDao;
    @Autowired
    protected TableGroupDao tableGroupDao;
    @Autowired
    protected TableGroupService tableGroupService;


    @Test
    @DisplayName("새 주문 테이블을 생성한다")
    void create() {
        // given
        int numberOfGuests = 999;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        // when
        OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다")
    void list(){
        // given
        int default_data_size = 8;

        // when
        List<OrderTable> orderTables = tableService.list();
        OrderTable firstTable = orderTables.get(0);

        // then
        assertAll(
            () -> assertThat(orderTables).hasSize(default_data_size),
            () -> assertThat(firstTable.getId()).isEqualTo(1),
            () -> assertThat(firstTable.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(firstTable.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 점유 여부를 변경한다")
    void changeIsEmpty() {

    }

    @Test
    @DisplayName("등록되지 않은 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithNonRegisteredOrderTable() {
        // given
        Long fakeOrderTableId = 999L;
        OrderTable orderTable = new OrderTable();

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(fakeOrderTableId, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹이 설정된 테이블의 점유 여부를 변경할 수 없다")
    void changeIsEmptyWithoutTableGroup() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable createdOrderTable = tableService.create(orderTable);

       // tableGroupService.create()

    }
}
