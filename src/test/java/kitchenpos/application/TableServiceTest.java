package kitchenpos.application;

import static kitchenpos.application.fixture.OrderFixture.UNSAVED_ORDER;
import static kitchenpos.application.fixture.OrderTableFixture.INVALID_NUMBER_OF_GUEST;
import static kitchenpos.application.fixture.OrderTableFixture.NUMBER_OF_GUEST;
import static kitchenpos.application.fixture.OrderTableFixture.makeOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.TABLE_GROUP_ID_FOR_TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.OrderTableDao;
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
    private OrderService orderService;

    @DisplayName("테이블을 생성한다. empty유무와 상관없이 생성할 수 있다.")
    @ParameterizedTest
    @MethodSource("argsOfCreate")
    void create(OrderTable table) {
        OrderTable savedTable = tableService.create(table);
        Optional<OrderTable> savedOrderTable = orderTableDao.findById(savedTable.getId());

        assertThat(savedOrderTable).isPresent();
    }

    static Stream<Arguments> argsOfCreate() {
        return Stream.of(
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, true, TABLE_GROUP_ID_FOR_TEST)),
                Arguments.of(makeOrderTable(NUMBER_OF_GUEST, false, TABLE_GROUP_ID_FOR_TEST))
        );
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        int numberOfSavedTableBeforeCreate = tableService.list().size();
        tableService.create(makeOrderTable(NUMBER_OF_GUEST, true, TABLE_GROUP_ID_FOR_TEST));

        int numberOfSavedTable = tableService.list().size();

        assertThat(numberOfSavedTableBeforeCreate + 1).isEqualTo(numberOfSavedTable);
    }

    @DisplayName("테이블을 empty상태로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTable savedTable = tableService.create(makeOrderTable(NUMBER_OF_GUEST, false, TABLE_GROUP_ID_FOR_TEST));
        OrderTable changedOrderTable = tableService.changeEmpty(savedTable.getId(),
                makeOrderTable(NUMBER_OF_GUEST, true, null));

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹에 테이블이 속해있을떄 empty상태로 바꾸면 예외가 발생한다.")
    @Test
    void changeEmpty_Exception_TableGroup() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, false, TABLE_GROUP_ID_FOR_TEST);
        OrderTable savedTable = orderTableDao.save(orderTable);
        assertThatThrownBy(
                () -> tableService.changeEmpty(savedTable.getId(), makeOrderTable(NUMBER_OF_GUEST, true, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블과 관련된 주문이 모두 완료상태가 아닐떄 테이블을 empty로 바꾸면 예외가 발생한다.")
    @Test
    void changeEmpty_Exception_Order_Not_Completion() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, false, null);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        UNSAVED_ORDER.setOrderTableId(savedOrderTable.getId());
        orderService.create(UNSAVED_ORDER);

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), makeOrderTable(NUMBER_OF_GUEST, true, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = orderTableDao.save(makeOrderTable(NUMBER_OF_GUEST, false, null));
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                makeOrderTable(NUMBER_OF_GUEST + 1, false, null));

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(NUMBER_OF_GUEST + 1);
    }

    @DisplayName("EMPTY상태의 테이블 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Empty_Table() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, true, null);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                makeOrderTable(NUMBER_OF_GUEST + 1, false, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 0미만으로 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_Exception_Invalid_Number() {
        OrderTable orderTable = makeOrderTable(NUMBER_OF_GUEST, false, null);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                makeOrderTable(INVALID_NUMBER_OF_GUEST, false, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
