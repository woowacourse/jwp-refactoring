package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = tableService.create(new OrderTable(2, false));

        assertThat(orderTable.getId()).isNotNull();
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {
        tableService.create(new OrderTable(2, false));

        assertThat(tableService.list()).hasSize(1);
    }

    @DisplayName("주문 테이블의 상태를 empty로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = tableService.create(new OrderTable(2, false));
        OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), new OrderTable(0, true));

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 상태를 바꿀때, table group id가 있으면 예외가 발생한다.")
    @Test
    void changeEmptyFailureWhenTableGroupIdExists() {
        //given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));

        //when
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 2, false));

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTable(0, true))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 바꿀때, 주문 상태가 COOKING이나 MEAL 이면 예외가 발생한다.")
    @MethodSource
    @ParameterizedTest
    void changeEmptyFailureWhenOrderStatusIsCookingOrMeal(OrderStatus orderStatus) {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
        orderDao.save(new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now()));

        //then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTable(0, true))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<OrderStatus> changeEmptyFailureWhenOrderStatusIsCookingOrMeal() {
        return Stream.of(OrderStatus.COOKING, OrderStatus.MEAL);
    }

    @DisplayName("손님의 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final int numberOfGuests = 3;

        OrderTable orderTable = tableService.create(new OrderTable(2, false));
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(
                numberOfGuests, false));

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("손님의 수가 음수이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsFailureWhenNumberIsNegative() {
        //given
        final int numberOfGuests = -1;
        OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, false));

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(
                numberOfGuests, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블이 비어있으면(empty is true) 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsFailureWhenSavedOrderTableIsEmpty() {
        //given
        final int numberOfGuests = 4;
        OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, true));

        //then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(numberOfGuests, false))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
