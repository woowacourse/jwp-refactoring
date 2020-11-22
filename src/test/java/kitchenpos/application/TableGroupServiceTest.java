package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.InvalidOrderTableIdsException;
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
        this.orderTable1 = createSavedOrderTable(NumberOfGuests.from(0), true);
        this.orderTable2 = createSavedOrderTable(NumberOfGuests.from(0), true);
    }

    @DisplayName("새로운 단체 지정 생성")
    @Test
    void createTableGroupTest() {
        List<Long> orderTableIds = Arrays.asList(this.orderTable1.getId(), this.orderTable2.getId());
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        TableGroupResponse tableGroupResponse = this.tableGroupService.createTableGroup(tableGroupCreateRequest);

        assertAll(
                () -> assertThat(tableGroupResponse).isNotNull(),
                () -> assertThat(tableGroupResponse.getCreatedDate()).isNotNull()
                        .isInstanceOf(LocalDateTime.class),
                () -> assertThat(tableGroupResponse.getOrderTableResponses()).hasSize(orderTableIds.size())
        );
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 없으면 예외 발생")
    @Test
    void createTableGroupWithNoOrderTableThenThrowException() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.emptyList());

        assertThatThrownBy(() -> this.tableGroupService.createTableGroup(tableGroupCreateRequest)).isInstanceOf(InvalidOrderTableIdsException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 1개면 예외 발생")
    @Test
    void createTableGroupWithZeroOrOneOrderTableThenThrowException() {
        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(Collections.singletonList(this.orderTable1.getId()));

        assertThatThrownBy(() -> this.tableGroupService.createTableGroup(tableGroupCreateRequest)).isInstanceOf(InvalidOrderTableIdsException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 존재하지 않는 테이블이면 예외 발생")
    @Test
    void createTableGroupWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTable savedOrderTable = createSavedOrderTable(NumberOfGuests.from(0), true);

        TableGroupCreateRequest tableGroupCreateRequest =
                new TableGroupCreateRequest(Arrays.asList(notExistOrderTableId, savedOrderTable.getId()));

        assertThatThrownBy(() -> this.tableGroupService.createTableGroup(tableGroupCreateRequest)).isInstanceOf(InvalidOrderTableIdsException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 주문을 등록할 수 있으면(빈 테이블이 아니면) 예외 발생")
    @Test
    void createTableGroupWithNotEmptyOrderTableThenThrowException() {
        boolean isEmpty = false;
        OrderTable orderTable1 = createSavedOrderTable(NumberOfGuests.from(0), isEmpty);
        OrderTable orderTable2 = createSavedOrderTable(NumberOfGuests.from(0), !isEmpty);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        assertThatThrownBy(() -> this.tableGroupService.createTableGroup(tableGroupCreateRequest)).isInstanceOf(InvalidOrderTableException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블에 다른 단체 지정이 존재하면 예외 발생")
    @Test
    void createTableGroupWithOrderTableInOtherTableGroupThenThrowException() {
        TableGroup savedTableGroup = createSavedTableGroup();
        OrderTable orderTable = new OrderTable(NumberOfGuests.from(0), true);
        orderTable.setTableGroup(savedTableGroup);
        OrderTable orderTable1 = this.orderTableRepository.save(orderTable);
        OrderTable orderTable2 = createSavedOrderTable(NumberOfGuests.from(0), true);
        List<Long> orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId());

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        assertThatThrownBy(() -> this.tableGroupService.createTableGroup(tableGroupCreateRequest)).isInstanceOf(InvalidOrderTableException.class);
    }

    @DisplayName("특정 단체 지정을 제거하면 소속되었던 테이블에는 존재하는 단체 지정이 없어야 하며 동시에 주문을 등록할 수 있어야(빈 테이블이 아니어야) 한다")
    @Test
    void ungroupTest() {
        List<Long> orderTableIds = Arrays.asList(this.orderTable1.getId(), this.orderTable2.getId());
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        TableGroupResponse tableGroupResponse = this.tableGroupService.createTableGroup(tableGroupCreateRequest);
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
        createSavedOrder(this.orderTable1, OrderStatus.COOKING);
        createSavedOrder(this.orderTable2, OrderStatus.MEAL);
        List<Long> orderTableIds = Arrays.asList(this.orderTable1.getId(), this.orderTable2.getId());
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(orderTableIds);

        TableGroupResponse tableGroupResponse = this.tableGroupService.createTableGroup(tableGroupCreateRequest);

        assertThatThrownBy(() -> this.tableGroupService.ungroup(tableGroupResponse.getId())).isInstanceOf(InvalidOrderTableException.class);
    }

    private OrderTable createSavedOrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);
        return this.orderTableRepository.save(orderTable);
    }

    private TableGroup createSavedTableGroup() {
        OrderTable orderTable1 = createSavedOrderTable(NumberOfGuests.from(0), true);
        OrderTable orderTable2 = createSavedOrderTable(NumberOfGuests.from(0), true);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        return this.tableGroupRepository.save(tableGroup);
    }

    private Order createSavedOrder(OrderTable orderTable, OrderStatus orderStatus) {
        Order order = new Order(orderTable, orderStatus, LocalDateTime.now());

        return this.orderRepository.save(order);
    }
}