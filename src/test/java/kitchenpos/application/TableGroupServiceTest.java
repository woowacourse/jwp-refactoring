package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        this.orderTable1 = createSavedOrderTable(0, true);
        this.orderTable2 = createSavedOrderTable(0, true);
    }

    @DisplayName("새로운 단체 지정 생성")
    @Test
    void createTableGroupTest() {
        OrderTableCreateRequest orderTableCreateRequest1 =
                new OrderTableCreateRequest(this.orderTable1.getId(), this.orderTable1.getNumberOfGuests(),
                                            this.orderTable1.isEmpty());
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(this.orderTable2.getId(), this.orderTable2.getNumberOfGuests(),
                                            this.orderTable2.isEmpty());
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1,
                                                                               orderTableCreateRequest2);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableCreateRequests);

        TableGroupResponse tableGroupResponse = this.tableGroupService.create(tableGroupCreateRequest);

        assertAll(
                () -> assertThat(tableGroupResponse).isNotNull(),
                () -> assertThat(tableGroupResponse.getCreatedDate()).isNotNull()
                        .isInstanceOf(LocalDateTime.class),
                () -> assertThat(tableGroupResponse.getOrderTableResponses()).hasSize(orderTableCreateRequests.size())
        );
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 없으면 예외 발생")
    @Test
    void createTableGroupWithNoOrderTableThenThrowException() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.emptyList());

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroupCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 1개면 예외 발생")
    @Test
    void createTableGroupWithZeroOrOneOrderTableThenThrowException() {
        OrderTableCreateRequest orderTableCreateRequest =
                new OrderTableCreateRequest(this.orderTable1.getId(), this.orderTable1.getNumberOfGuests(),
                                            this.orderTable1.isEmpty());
        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(Collections.singletonList(orderTableCreateRequest));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroupCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 존재하지 않는 테이블이면 예외 발생")
    @Test
    void createTableGroupWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(notExistOrderTableId, 0, true);

        OrderTable savedOrderTable = createSavedOrderTable(0, true);
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(savedOrderTable.getId(), savedOrderTable.getNumberOfGuests(),
                                            savedOrderTable.isEmpty());

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroupCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 주문을 등록할 수 있으면(빈 테이블이 아니면) 예외 발생")
    @Test
    void createTableGroupWithNotEmptyOrderTableThenThrowException() {
        boolean isEmpty = false;
        OrderTable orderTable1 = createSavedOrderTable(0, isEmpty);
        OrderTable orderTable2 = createSavedOrderTable(0, !isEmpty);
        OrderTableCreateRequest orderTableCreateRequest1 =
                new OrderTableCreateRequest(orderTable1.getId(), orderTable1.getNumberOfGuests(),
                                            orderTable1.isEmpty());
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(orderTable2.getId(), orderTable2.getNumberOfGuests(),
                                            orderTable2.isEmpty());
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1,
                                                                               orderTableCreateRequest2);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableCreateRequests);

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroupCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블에 다른 단체 지정이 존재하면 예외 발생")
    @Test
    void createTableGroupWithOrderTableInOtherTableGroupThenThrowException() {
        TableGroup savedTableGroup = createSavedTableGroup();
        OrderTable orderTable = new OrderTable(0, true);
        orderTable.setTableGroup(savedTableGroup);
        OrderTable orderTable1 = this.orderTableRepository.save(orderTable);
        OrderTable orderTable2 = createSavedOrderTable(0, true);

        OrderTableCreateRequest orderTableCreateRequest1 =
                new OrderTableCreateRequest(orderTable1.getId(), orderTable1.getNumberOfGuests(),
                                            orderTable1.isEmpty());
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(orderTable2.getId(), orderTable2.getNumberOfGuests(), orderTable2.isEmpty());
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1,
                                                                               orderTableCreateRequest2);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableCreateRequests);

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroupCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 단체 지정을 제거하면 소속되었던 테이블에는 존재하는 단체 지정이 없어야 하며 동시에 주문을 등록할 수 있어야(빈 테이블이 아니어야) 한다")
    @Test
    void ungroupTest() {
        OrderTableCreateRequest orderTableCreateRequest1 =
                new OrderTableCreateRequest(this.orderTable1.getId(), this.orderTable1.getNumberOfGuests(),
                                            this.orderTable1.isEmpty());
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(this.orderTable2.getId(), this.orderTable2.getNumberOfGuests(),
                                            this.orderTable2.isEmpty());
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1,
                                                                               orderTableCreateRequest2);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableCreateRequests);

        TableGroupResponse tableGroupResponse = this.tableGroupService.create(tableGroupCreateRequest);
        this.tableGroupService.ungroup(tableGroupResponse.getId());

        OrderTable savedOrderTable1 =
                this.orderTableRepository.findById(this.orderTable1.getId()).orElseThrow(IllegalArgumentException::new);
        OrderTable savedOrderTable2 =
                this.orderTableRepository.findById(this.orderTable2.getId()).orElseThrow(IllegalArgumentException::new);

        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroup()).isNull(),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroup()).isNull(),
                () -> assertThat(savedOrderTable2.isEmpty()).isFalse()
        );
    }

    @DisplayName("특정 단체 지정을 제거할 때 소속 테이블의 주문 상태가 조리 혹은 식사면 예외 발생")
    @Test
    void ungroupWithOrderTableOfCookingOrMealThenThrowException() {
        createSavedOrder(this.orderTable1, OrderStatus.COOKING.name());
        createSavedOrder(this.orderTable2, OrderStatus.MEAL.name());

        OrderTableCreateRequest orderTableCreateRequest1 =
                new OrderTableCreateRequest(this.orderTable1.getId(), this.orderTable1.getNumberOfGuests(), this.orderTable1.isEmpty());
        OrderTableCreateRequest orderTableCreateRequest2 =
                new OrderTableCreateRequest(this.orderTable2.getId(), this.orderTable2.getNumberOfGuests(), this.orderTable2.isEmpty());
        List<OrderTableCreateRequest> orderTableCreateRequests = Arrays.asList(orderTableCreateRequest1, orderTableCreateRequest2);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableCreateRequests);

        TableGroupResponse tableGroupResponse = this.tableGroupService.create(tableGroupCreateRequest);

        assertThatThrownBy(() -> this.tableGroupService.ungroup(tableGroupResponse.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createSavedOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return this.orderTableRepository.save(orderTable);
    }

    private TableGroup createSavedTableGroup() {
        OrderTable orderTable1 = createSavedOrderTable(0, true);
        OrderTable orderTable2 = createSavedOrderTable(0, true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        return this.tableGroupRepository.save(tableGroup);
    }

    private Order createSavedOrder(OrderTable orderTable, String orderStatus) {
        Order order = new Order(orderTable);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus);

        return this.orderRepository.save(order);
    }
}