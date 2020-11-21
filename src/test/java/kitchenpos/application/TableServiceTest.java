package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableChangeEmptyRequest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableChangeNumberOfGuestsRequest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableRequest;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

public class TableServiceTest extends AbstractServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequest = createOrderTableRequest(true, 0);

        OrderTableResponse savedOrderTable = tableService.create(orderTableCreateRequest);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable.getNumberOfGuests())
                .isEqualTo(orderTableCreateRequest.getNumberOfGuests()),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableCreateRequest.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<OrderTableResponse> orderTables = OrderTableResponse.listOf(Arrays.asList(
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null)),
            orderTableDao.save(createOrderTable(null, true, 0, null))
        ));

        List<OrderTableResponse> allOrderTables = tableService.list();

        assertThat(allOrderTables).usingFieldByFieldElementComparator().containsAll(orderTables);
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @MethodSource("provideEmpty")
    @ParameterizedTest
    void changeEmpty(boolean oldEmpty, boolean newEmpty) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, oldEmpty, 0, null));
        OrderTableChangeEmptyRequest orderTableRequest = createOrderTableChangeEmptyRequest(
            newEmpty
        );

        OrderTableResponse changedOrderTable = tableService
            .changeEmpty(orderTable.getId(), orderTableRequest);

        assertThat(changedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    private static Stream<Arguments> provideEmpty() {
        return Stream.of(
            Arguments.of(true, true),
            Arguments.of(true, false),
            Arguments.of(false, true),
            Arguments.of(false, false)
        );
    }

    @DisplayName("단체 지정한 주문 테이블은 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_throws_exception() {
        TableGroup tableGroup = tableGroupDao
            .save(createTableGroup(null, LocalDateTime.now()));
        OrderTable orderTable = orderTableDao
            .save(createOrderTable(null, true, 0, tableGroup.getId()));
        OrderTableChangeEmptyRequest orderTableRequest = createOrderTableChangeEmptyRequest(false);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableService
            .changeEmpty(orderTable.getId(), orderTableRequest));
    }

    @DisplayName("조리 또는 식사 중인 주문 테이블은 빈 테이블 여부를 변경할 수 없다.")
    @MethodSource("provideOrderStatus")
    @ParameterizedTest
    void changeEmpty_throws_exception2(OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, true, 0, null));
        Order order = orderDao
            .save(createOrder(null, LocalDateTime.now(), orderStatus, orderTable.getId()));
        OrderTableChangeEmptyRequest orderTableRequest = createOrderTableChangeEmptyRequest(false);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableService
            .changeEmpty(orderTable.getId(), orderTableRequest));
    }

    private static Stream<Arguments> provideOrderStatus() {
        return Stream.of(
            Arguments.of(OrderStatus.COOKING),
            Arguments.of(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, 0, null));
        OrderTableChangeNumberOfGuestsRequest orderTableRequest = createOrderTableChangeNumberOfGuestsRequest(
            1
        );

        OrderTableResponse changedOrderTable = tableService
            .changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        assertThat(changedOrderTable.getNumberOfGuests())
            .isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("손님 수가 0 미만이면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throws_exception() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, 0, null));
        OrderTableChangeNumberOfGuestsRequest orderTableRequest = createOrderTableChangeNumberOfGuestsRequest(
            -1
        );

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest));
    }

    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_throws_exception2() {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, true, 0, null));
        OrderTableChangeNumberOfGuestsRequest orderTableRequest = createOrderTableChangeNumberOfGuestsRequest(
            1
        );

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest));
    }
}
