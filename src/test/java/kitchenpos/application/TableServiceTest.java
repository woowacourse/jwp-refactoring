package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE1;
import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.support.DatabaseCleaner;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("OrderTable을 생성한다.")
    @Test
    void create() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTableRequest);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("OrderTable들을 조회한다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = new OrderTable(3, true);
        orderTableRepository.save(orderTable);

        // when
        final List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables.size()).isEqualTo(1);
    }

    @DisplayName("orderTableId가 존재하지 않은 경우 empty 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_notExistOrderTableId_throwsException() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(9L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 단체 지정이 된 경우 예외가 발생한다.")
    @Test
    void changeEmpty_tableGroupIdNonNull_throwsException() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable));
        tableGroupRepository.save(tableGroup);

        final OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 포함된 주문이 요리중이거나 식사 중인 경우 예외가 발생한다.")
    @Test
    void changeEmpty_orderStatusCookingOrMeal_throwsException() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(2, false));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable));
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderLineItem orderLineItem = new OrderLineItem(3, "menuName", BigDecimal.valueOf(3000));
        final Order order = new Order(savedOrderTable, OrderStatus.COOKING, LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderRepository.save(order);

        final OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable의 empty 상태를 정상적으로 변경한다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(3, true));
        final OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        // when
        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        // then
        assertAll(
                () -> assertThat(changedOrderTable.getId()).isNotNull(),
                () -> assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블의 방문자 수를 0보다 작은수로 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_numberOfGuestsLessThanZero_throwsException() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTableId가 존재하지 않은 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_notExistOrderTableId_throwsException() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(9L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_orderTableEmpty_throwsException() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문자 수를 정상적으로 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable savedOrderTable = orderTableRepository.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderTable orderTable = ORDER_TABLE1.create();
        final OrderTableRequest orderTableRequest = new OrderTableRequest(3);

        // when
        final OrderTable updatedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                orderTableRequest);

        // then
        assertAll(
                () -> assertThat(updatedOrderTable.getId()).isNotNull(),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(3)
        );
    }
}
