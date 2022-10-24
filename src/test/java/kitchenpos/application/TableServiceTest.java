package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.DataSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private DataSupport dataSupport;

    @DisplayName("새로운 테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        // when, then
        assertThatCode(() -> tableService.create(orderTable))
                .doesNotThrowAnyException();
    }

    @DisplayName("테이블의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given, when
        final List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("테이블이 비었는지 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        // when
        tableService.changeEmpty(orderTableId, notEmptyOrderTable);

        // then
        final OrderTable changedTable = dataSupport.findOrderTable(orderTableId);
        assertThat(changedTable.isEmpty()).isFalse();
    }

    @DisplayName("존재하지 않는 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenTableNotFound() {
        // given
        final OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(100L, notEmptyOrderTable));
    }

    @DisplayName("단체 지정된 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenHasGroup() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(savedTableGroup.getId(), 0, true);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyOrderTable));
    }

    @DisplayName("조리중인 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenCooking() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        dataSupport.saveOrder(orderTableId, OrderStatus.COOKING.name());

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyOrderTable));
    }

    @DisplayName("식사중인 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenMeal() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable notEmptyOrderTable = new OrderTable();
        notEmptyOrderTable.setEmpty(false);

        dataSupport.saveOrder(orderTableId, OrderStatus.MEAL.name());

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyOrderTable));
    }

    @DisplayName("테이블의 고객 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, false);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable tableWith2Guests = new OrderTable();
        tableWith2Guests.setNumberOfGuests(2);

        // when
        tableService.changeNumberOfGuests(orderTableId, tableWith2Guests);

        // then
        final OrderTable changedTable = dataSupport.findOrderTable(orderTableId);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("존재하지 않는 테이블의 고객 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifTableNotFound() {
        final OrderTable tableWith2Guests = new OrderTable();
        tableWith2Guests.setNumberOfGuests(2);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(100L, tableWith2Guests));
    }

    @DisplayName("테이블의 고객 수를 0보다 작게 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifGuestsUnder0() {
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, false);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable tableWithMinusGuests = new OrderTable();
        tableWithMinusGuests.setNumberOfGuests(-1);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableWithMinusGuests));
    }

    @DisplayName("빈 테이블의 고객 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifEmpty() {
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        final OrderTable tableWith2Guests = new OrderTable();
        tableWith2Guests.setNumberOfGuests(2);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, tableWith2Guests));
    }
}
