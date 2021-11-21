package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItem.OrderLineItemBuilder;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.InvalidOrderTableSizeException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableEmptyGroupIdException;
import kitchenpos.exception.UnGroupNotCompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private static OrderTable savedOrderTable1;
    private static OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {

        OrderLineItem orderLineItem = new OrderLineItemBuilder()
                .setQuantity(4)
                .build();

        Order order = new Order.OrderBuilder()
                .setOrderStatus(OrderStatus.COMPLETION.name())
                .setOrderLineItems(List.of(orderLineItem))
                .build();

        OrderTable orderTable1 = new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(4)
                .setOrders(List.of(order))
                .setEmpty(true)
                .build();
        OrderTable orderTable2 = new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(3)
                .setOrders(List.of(order))
                .setEmpty(true)
                .build();

        savedOrderTable1 = orderTableRepository.save(orderTable1);
        savedOrderTable2 = orderTableRepository.save(orderTable2);
    }

    @DisplayName("단체 지정을 등록할 수 있다.")
    @Test
    void create() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                        new OrderTableIdRequest(savedOrderTable1.getId()),
                        new OrderTableIdRequest(savedOrderTable2.getId()))
        );

        //when
        TableGroupResponse actual = tableGroupService.create(tableGroupRequest);
        //then
        assertThat(actual.getOrderTableResponses().get(0).getNumberOfGuests()).isEqualTo(4);
        assertThat(actual.getOrderTableResponses().get(1).getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("단체 지정 등록 실패 - 주문 테이블이 없거나 하나인 경우")
    @Test
    void createFailInvalidOrderTableCount() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                new OrderTableIdRequest(savedOrderTable1.getId()))
        );
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @DisplayName("단체 지정 등록 실패 - 존재하지 않는 주문 테이블인 경우")
    @Test
    void createFailNotExistOrderTable() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                new OrderTableIdRequest(savedOrderTable1.getId()),
                new OrderTableIdRequest(1000L))
        );
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @DisplayName("단체 지정 등록 실패 - 빈 테이블이 아닌 경우")
    @Test
    void createFailNotEmptyTable() {
        //given
        OrderTable notEmptyOrderTable = new OrderTable.OrderTableBuilder()
                .setEmpty(false)
                .setNumberOfGuests(4)
                .build();

        OrderTable savedNotEmptyOrderTable = orderTableRepository.save(notEmptyOrderTable);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                new OrderTableIdRequest(savedOrderTable1.getId()),
                new OrderTableIdRequest(savedNotEmptyOrderTable.getId()))
        );
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(OrderTableEmptyGroupIdException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void ungroup() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                new OrderTableIdRequest(savedOrderTable1.getId()),
                new OrderTableIdRequest(savedOrderTable2.getId()))
        );

        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        //when
        tableGroupService.ungroup(tableGroupResponse.getId());
        //then
        assertThat(savedOrderTable1.getTableGroup()).isNull();
        assertThat(savedOrderTable2.getTableGroup()).isNull();
        assertThat(savedOrderTable1.isEmpty()).isFalse();
        assertThat(savedOrderTable2.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정을 해제 실패 - 계산 완료가 안된 테이블이 있는 경우")
    @Test
    void ungroupFailExistNotCompletionTable() {
        //given
        OrderLineItem orderLineItem = new OrderLineItemBuilder()
                .setQuantity(4)
                .build();

        Order orderWithOrderStatusMeal = new Order.OrderBuilder()
                .setOrderStatus(OrderStatus.MEAL.name())
                .setOrderLineItems(List.of(orderLineItem))
                .build();

        OrderTable orderTableWithOrderStatusMeal = new OrderTable.OrderTableBuilder()
                .setNumberOfGuests(4)
                .setOrders(List.of(orderWithOrderStatusMeal))
                .setEmpty(true)
                .build();

        OrderTable savedOrderTableWithOrderStatusMeal = orderTableRepository.save(orderTableWithOrderStatusMeal);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(
                new OrderTableIdRequest(savedOrderTable1.getId()),
                new OrderTableIdRequest(savedOrderTableWithOrderStatusMeal.getId()))
        );

        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(UnGroupNotCompletionException.class);
    }
}