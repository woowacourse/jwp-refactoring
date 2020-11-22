package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.ordertable.OrderTableChangeEmptyRequest;
import kitchenpos.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("새로운 테이블 생성")
    @ParameterizedTest
    @CsvSource(value = {"0,false", "0,true", "2,false", "2,true"})
    void createTableTest(int numberOfGuests, boolean isEmpty) {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(numberOfGuests, isEmpty);

        OrderTableResponse orderTableResponse = this.tableService.createOrderTable(orderTableCreateRequest);

        assertAll(
                () -> assertThat(orderTableResponse).isNotNull(),
                () -> assertThat(orderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableCreateRequest.getNumberOfGuests()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableCreateRequest.isEmpty())
        );
    }

    @DisplayName("존재하는 모든 테이블을 조회")
    @Test
    void listTableTest() {
        OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(1, true);
        OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(3, false);
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1,
                                                                               orderTableCreateRequest2);
        orderTableCreateRequests.forEach(orderTableCreateRequest -> this.tableService.createOrderTable(orderTableCreateRequest));

        List<OrderTableResponse> orderTableResponses = this.tableService.listAllOrderTables();

        assertThat(orderTableResponses).hasSize(orderTableCreateRequests.size());
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부, 즉 빈 테이블 여부를 변경")
    @ParameterizedTest
    @CsvSource(value = {"2,false", "2,true"})
    void changeEmptyTest(int numberOfGuests, boolean isEmpty) {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(numberOfGuests, isEmpty);
        OrderTableResponse orderTableCreateResponse = this.tableService.createOrderTable(orderTableCreateRequest);
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(!isEmpty);

        OrderTableResponse orderTableChangeResponse = this.tableService.changeEmpty(orderTableCreateResponse.getId(),
                                                                                    orderTableChangeEmptyRequest);

        assertAll(
                () -> assertThat(orderTableChangeResponse.isEmpty()).isEqualTo(orderTableChangeEmptyRequest.isEmpty()),
                () -> assertThat(orderTableChangeResponse.isEmpty()).isEqualTo(!orderTableCreateRequest.isEmpty()),
                () -> assertThat(orderTableChangeResponse.getNumberOfGuests()).isEqualTo(orderTableCreateRequest.getNumberOfGuests())
        );
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블이 존재하지 않는 테이블이면 예외 발생")
    @Test
    void changeEmptyWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

        assertThatThrownBy(() -> this.tableService.changeEmpty(notExistOrderTableId, orderTableChangeEmptyRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블에 단체 지정이 있으면 예외 발생")
    @Test
    void changeEmptyWithOrderTableInTableGroupThenThrowException() {
        OrderTable orderTable1 = new OrderTable(NumberOfGuests.from(0), false);
        OrderTable orderTable2 = new OrderTable(NumberOfGuests.from(2), true);
        TableGroup savedTableGroup = createSavedTableGroup(LocalDateTime.now(), Arrays.asList(orderTable1,
                                                                                              orderTable2));

        orderTable1.setTableGroup(savedTableGroup);
        OrderTable savedOrderTable = this.orderTableRepository.save(orderTable1);

        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(false);

        assertThatThrownBy(() -> this.tableService.changeEmpty(savedOrderTable.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블의 주문 상태가 조리 혹은 식사이면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithOrderTableInCookingOrMealThenThrowException(String orderStatus) {
        OrderTable orderTable1 = new OrderTable(NumberOfGuests.from(0), false);
        OrderTable savedOrderTable1 = this.orderTableRepository.save(orderTable1);
        createSavedOrder(savedOrderTable1, OrderStatus.from(orderStatus));

        OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);

        assertThatThrownBy(() -> this.tableService.changeEmpty(savedOrderTable1.getId(), orderTableChangeEmptyRequest))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, false);
        OrderTableResponse orderTableCreateResponse = this.tableService.createOrderTable(orderTableCreateRequest);
        OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(2);

        OrderTableResponse orderTableChangeResponse =
                this.tableService.changeNumberOfGuests(orderTableCreateResponse.getId(), orderTableChangeNumberOfGuestsRequest);

        assertThat(orderTableChangeResponse.getNumberOfGuests()).isEqualTo(orderTableChangeNumberOfGuestsRequest.getNumberOfGuests());
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 손님 수가 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10})
    void changeNumberOfGuestsUnderZeroThenThrowException(int invalidNumberOfGuests) {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, false);
        OrderTableResponse orderTableResponse = this.tableService.createOrderTable(orderTableCreateRequest);
        OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(invalidNumberOfGuests);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(orderTableResponse.getId(), orderTableChangeNumberOfGuestsRequest))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 특정 주문 테이블이 존재하는 주문 테이블이 아니면 예외 발생")
    @Test
    void changeNumberOfGuestsWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(0);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(notExistOrderTableId, orderTableChangeNumberOfGuestsRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 특정 주문 테이블이 주문을 등록할 수 없으면(빈 테이블이면) 예외 발생")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTableThenThrowException() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(0, true);
        OrderTableResponse orderTableResponse = this.tableService.createOrderTable(orderTableCreateRequest);
        OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest = new OrderTableChangeNumberOfGuestsRequest(2);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(orderTableResponse.getId(),
                                                                        orderTableChangeNumberOfGuestsRequest))
                .isInstanceOf(InvalidOrderTableException.class);
    }

    private Order createSavedOrder(OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());
        return this.orderRepository.save(order);
    }

    private TableGroup createSavedTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        return this.tableGroupRepository.save(tableGroup);
    }
}