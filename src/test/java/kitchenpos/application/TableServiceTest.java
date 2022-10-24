package kitchenpos.application;

import static kitchenpos.application.OrderFixture.UNSAVED_ORDER;
import static kitchenpos.application.OrderTableFixture.SAVED_ORDER_TABLE_EMPTY_FIRST;
import static kitchenpos.application.OrderTableFixture.SAVED_ORDER_TABLE_NOT_EMPTY_FIRST;
import static kitchenpos.application.OrderTableFixture.UNSAVED_ORDER_TABLE_EMPTY;
import static kitchenpos.application.OrderTableFixture.UNSAVED_ORDER_TABLE_NOT_EMPTY;
import static kitchenpos.application.TableGroupFixture.SAVED_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("테이블을 생성한다. empty유무와 상관없이 생성할 수 있다.")
    @ParameterizedTest
    @MethodSource("argsOfCreate")
    void create(OrderTable table) {
        OrderTable savedTable = tableService.create(table);
        List<OrderTable> tables = tableService.list();

        assertThat(tables).contains(savedTable);
    }

    static Stream<Arguments> argsOfCreate() {
        return Stream.of(
                Arguments.of(UNSAVED_ORDER_TABLE_EMPTY),
                Arguments.of(UNSAVED_ORDER_TABLE_NOT_EMPTY)
        );
    }

    @DisplayName("테이블을 empty상태로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTable savedTable = tableService.create(UNSAVED_ORDER_TABLE_NOT_EMPTY);
        tableService.changeEmpty(savedTable.getId(), UNSAVED_ORDER_TABLE_EMPTY);
    }

    @DisplayName("테이블 그룹에 테이블이 속해있을떄 empty상태로 바꾸면 예외가 발생한다.")
    @Test
    void changeEmpty_Exception_TableGroup() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setTableGroupId(SAVED_TABLE_GROUP.getId());
        OrderTable savedTable = orderTableDao.save(UNSAVED_ORDER_TABLE_NOT_EMPTY);
        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), UNSAVED_ORDER_TABLE_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블과 관련된 주문이 모두 완료상태가 아닐떄 테이블을 empty로 바꾸면 예외가 발생한다.")
    @Test
    void changeEmpty_Exception_Order_Not_Completion() {
        UNSAVED_ORDER.setOrderTableId(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST.getId());
        orderService.create(UNSAVED_ORDER);

        assertThatThrownBy(() -> tableService.changeEmpty(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST.getId(), SAVED_ORDER_TABLE_EMPTY_FIRST))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST.getId(),
                UNSAVED_ORDER_TABLE_NOT_EMPTY);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(UNSAVED_ORDER_TABLE_NOT_EMPTY.getNumberOfGuests());
    }

    @DisplayName("EMPTY상태의 테이블 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Empty_Table() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(SAVED_ORDER_TABLE_EMPTY_FIRST.getId(), UNSAVED_ORDER_TABLE_NOT_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 0미만으로 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Invalid_Number() {
        UNSAVED_ORDER_TABLE_NOT_EMPTY.setNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(SAVED_ORDER_TABLE_NOT_EMPTY_FIRST.getId(), UNSAVED_ORDER_TABLE_NOT_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
