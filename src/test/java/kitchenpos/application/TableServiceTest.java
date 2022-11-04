package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kitchenpos.order.application.TableService;
import kitchenpos.order.application.dto.OrderTableRequest;
import kitchenpos.order.application.dto.OrderTableRequest.NumberOfGuest;
import kitchenpos.order.application.dto.OrderTableResponse;
import kitchenpos.order.domain.repository.OrderDao;
import kitchenpos.order.domain.repository.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
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
        OrderTableResponse acutal = tableService.create(new OrderTableRequest.Create(2, false));

        assertThat(acutal.getId()).isNotNull();
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void list() {
        tableService.create(new OrderTableRequest.Create(2, false));

        assertThat(tableService.list()).hasSize(1);
    }

    @DisplayName("주문 테이블의 상태를 empty로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest.Create(2, false));
        OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), new OrderTableRequest.Empty(true));

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 상태를 바꿀때, table group id가 있으면 예외가 발생한다.")
    @Test
    void changeEmptyFailureWhenTableGroupIdExists() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 2, false));

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTableRequest.Empty(true))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태를 바꿀때, 주문 상태가 COOKING이나 MEAL 이면 예외가 발생한다.")
    @CsvSource(value = {"MEAL", "COOKING"})
    @ParameterizedTest
    void changeEmptyFailureWhenOrderStatusIsCookingOrMeal(OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));
        orderDao.save(new Order(orderTable.getId(), orderStatus, LocalDateTime.now()));

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), new OrderTableRequest.Empty((true)))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final int numberOfGuests = 3;

        OrderTableResponse orderTable = tableService.create(new OrderTableRequest.Create(2, false));
        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(),
                new NumberOfGuest(numberOfGuests));

        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("손님의 수가 음수이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsFailureWhenNumberIsNegative() {
        final int numberOfGuests = -1;
        OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, false));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(),
                new NumberOfGuest(numberOfGuests)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블이 비어있으면(empty is true) 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsFailureWhenSavedOrderTableIsEmpty() {
        final int numberOfGuests = 4;
        OrderTable orderTable = orderTableDao.save(new OrderTable(numberOfGuests, true));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), new NumberOfGuest(numberOfGuests))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
