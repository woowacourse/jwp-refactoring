package kitchenpos.application;

import kitchenpos.Menu.domain.Menu;
import kitchenpos.Menu.domain.repository.MenuRepository;
import kitchenpos.Order.application.OrderService;
import kitchenpos.Order.domain.Order;
import kitchenpos.Order.domain.OrderLineItem;
import kitchenpos.Order.domain.OrderStatus;
import kitchenpos.OrderTable.application.TableGroupService;
import kitchenpos.OrderTable.application.TableService;
import kitchenpos.OrderTable.domain.OrderTable;
import kitchenpos.OrderTable.domain.TableGroup;
import kitchenpos.OrderTable.domain.repository.OrderTableRepository;
import kitchenpos.annotation.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IntegrationTest
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable notEmptyTable;
    private OrderTable emptyTable;

    private final List<OrderTable> validOrderTables = new ArrayList<>();

    @BeforeEach
    void setUp() {
        notEmptyTable = new OrderTable(false);
        emptyTable = new OrderTable(true);

        OrderTable orderTable1 = new OrderTable(1L);
        OrderTable orderTable2 = new OrderTable(2L);

        validOrderTables.add(orderTable1);
        validOrderTables.add(orderTable2);
    }

    @Test
    @DisplayName("OrderTable을 추가할 수 있다.")
    public void enrollOrderTable() {
        //given & when
        OrderTable orderTable = new OrderTable(10, false);

        //then
        assertDoesNotThrow(() -> tableService.create(orderTable));
    }

    @Test
    @DisplayName("등록된 OrderTable을 조회할 수 있다.")
    public void findAll() {
        //given
        List<OrderTable> orderTables = tableService.list();

        //when & then
        assertThat(orderTables).hasSize(8);
    }

    @Test
    @DisplayName("OrderTable Empty 수정 시, 존재하지않은 OrderTable Id가 주어져서는 안된다.")
    public void notExistOrderTableIdException() {
        assertThatThrownBy(() -> tableService.changeEmpty(10L, notEmptyTable.isEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TableGroup에 속한 OrderTable의 Empty는 수정할 수 없다.")
    public void cannotChangeTableStatusIncludedInTableGroup() {
        //given
        List<Long> tableIds = validOrderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        tableGroupService.create(tableIds);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(tableIds.get(0), emptyTable.isEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable이 현재 이용 중이면(COMPLETION이 아니면) 상태를 변경할 수 없다.")
    public void cannotChangeTableStatusWhenOrderActivated() {
        //given
        enrollOrder();

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, emptyTable.isEmpty()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable이 COMPLETION이면 상태를 변경할 수 있다.")
    public void changeTableStatusWhenOrderCompleted() {
        //given
        Order savedOrder = enrollOrder();

        //when
        Order completedOrder = new Order(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), completedOrder.getOrderStatus());

        //then
        assertDoesNotThrow(() -> tableService.changeEmpty(1L, emptyTable.isEmpty()));
    }

    @Test
    @DisplayName("OrderTable의 Empty 여부를 수정할 수 있다.")
    public void updateEmptyStatus() {
        assertDoesNotThrow(() -> tableService.changeEmpty(1L, notEmptyTable.isEmpty()));
    }

    @Test
    @DisplayName("NumberOfGuests를 0 미만의 값으로 수정할 수 없다.")
    public void cannotChangeNumberOfGuestsUnderZero() {
        //given & when
        OrderTable orderTable = new OrderTable(-1);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable.getNumberOfGuests()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("NumberOfGuests 수정 시, 존재하지않은 OrderTable Id가 주어져서는 안된다.")
    public void cannotChangeNumberOfGuestsWhenNonExistOrderTableId() {
        //given & when
        OrderTable orderTable = new OrderTable(5);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, orderTable.getNumberOfGuests()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("NumberOfGuests 수정 시, empty가 true인 OrderTable이어서는 안된다.")
    public void cannotChangeNumberOfGuestsWhenEmptyTable() {
        //given & when
        OrderTable orderTable = new OrderTable(5);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable.getNumberOfGuests()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable의 NumberOfGuests를 수정할 수 있다.")
    public void updateNumberOfGuests() {
        //given
        OrderTable orderTable = new OrderTable(5);

        //when
        tableService.changeEmpty(1L, notEmptyTable.isEmpty());

        //then
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(1L, orderTable.getNumberOfGuests()));
    }

    private Order enrollOrder() {
        tableService.changeEmpty(1L, notEmptyTable.isEmpty());
        Menu findMenu = menuRepository.findById(1L).get();
        OrderTable findOrderTable = orderTableRepository.findById(1L).get();

        OrderLineItem orderLineItem = new OrderLineItem(findMenu.getId(), 1L);
        Order order = new Order(findOrderTable.getId(), Collections.singletonList(orderLineItem));

        return orderService.create(order);
    }
}
