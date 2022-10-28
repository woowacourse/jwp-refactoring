package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.OrderTableRequest;
import kitchenpos.application.request.TableGroupRequest;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    private TableGroupService sut;
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        sut = new TableGroupService(orderDao, orderTableRepository, tableGroupRepository);
        tableService = new TableService(orderDao, orderTableRepository);
    }

    @DisplayName("새로운 단체 지정(table group)을 생성할 수 있다.")
    @Test
    void create() {
        // given
        final List<OrderTableRequest> orderTableRequests = toOrderTableRequests(tableService.list());
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), orderTableRequests);

        // when
        final TableGroupResponse createdTableGroup = sut.create(tableGroupRequest);

        // then
        assertThat(createdTableGroup).isNotNull();
        assertThat(createdTableGroup.getId()).isNotNull();
        final TableGroup foundTableGroup = tableGroupRepository.findById(createdTableGroup.getId()).get();
        assertThat(foundTableGroup)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderTables")
                .isEqualTo(createdTableGroup);
    }

    @DisplayName("새로운 테이블 그룹의 주문 테이블이 비어있거나 그룹화하려는 주문 테이블이 2개 보다 작을 수는 없다.")
    @Test
    void canNotCreateTableGroupLessThenTwoTable() {
        // given
        final List<OrderTable> orderTables = toOrderTables(tableService.list());
        final OrderTable orderTable = orderTables.get(0);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
        final TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), List.of(orderTableRequest));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정하려는 개별 주문 테이블이 실제 존재하는 주문 테이블이어야 한다.")
    @Test
    void canCreateTableGroupWhenExistOrderTable() {
        // given
        final OrderTableRequest orderTable1 = new OrderTableRequest(1, true);
        final OrderTableRequest orderTable2 = new OrderTableRequest(1, true);
        final TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(),
                List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 비어있고 이미 단체 지정되지 않은 경우에만 새롭게 지정할 수 있다.")
    @Test
    void canNotCreateTableGroupWhenAlreadyGrouping() {
        // given
        final List<OrderTableRequest> orderTables = toOrderTableRequests(tableService.list());
        sut.create(new TableGroupRequest(LocalDateTime.now(), orderTables));

        final OrderTableRequest orderTable1 = orderTables.get(0);
        final OrderTableRequest orderTable2 = orderTables.get(1);
        final TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(),
                List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 이미 그룹에 지정되어 그룹 id 를 가지고 있으면 그룹핑 할 수 없다.")
    @Test
    void canNotCreateTableGroupWhenTableInGroup() {
        // given
        final List<OrderTableRequest> orderTables = toOrderTableRequests(tableService.list());

        final TableGroupResponse createdTableGroup = sut.create(new TableGroupRequest(LocalDateTime.now(), orderTables));
        final OrderTable orderTable = new OrderTable(createdTableGroup.getId(), 0, true);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        final OrderTableRequest orderTableRequest = OrderTableRequest.from(savedOrderTable);
        orderTables.add(orderTableRequest);

        final TableGroupRequest tableGroup = new TableGroupRequest(LocalDateTime.now(), orderTables);

        // when & then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정(table group)을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final List<OrderTableRequest> orderTables = toOrderTableRequests(tableService.list());
        final TableGroupResponse tableGroup = sut.create(new TableGroupRequest(LocalDateTime.now(), orderTables));

        // when
        sut.ungroup(tableGroup.getId());

        // then
        final List<OrderTableResponse> results = tableService.list();
        assertThat(results)
                .hasSize(8)
                .extracting(OrderTableResponse::getTableGroupId)
                .containsExactly(null, null, null, null, null, null, null, null);
    }

    @DisplayName("이미 조리 중이거나 식사중인 테이블이 있으면 해제할 수 없다.")
    @Test
    void canNotUngroupWhenCookOrMeal() {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        final OrderTableRequest anotherOrderTableRequest = new OrderTableRequest(0, true);

        final OrderTableResponse createdOrderTable = tableService.create(orderTableRequest);
        final OrderTableResponse createdAnotherOrderTable = tableService.create(anotherOrderTableRequest);
        final TableGroupRequest tableGroup = createTableGroupRequest(LocalDateTime.now(), List.of(createdOrderTable, createdAnotherOrderTable));

        final TableGroupResponse createdTableGroup = sut.create(tableGroup);

        saveCookingOrder(createdOrderTable);

        // when & then
        assertThatThrownBy(() -> sut.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroupRequest createTableGroupRequest(final LocalDateTime createdDate,
                                                      final List<OrderTableResponse> orderTables) {
        final List<OrderTableRequest> orderTableRequests = orderTables.stream()
                .map(it -> new OrderTableRequest(it.getId(), it.getTableGroupId(), it.getNumberOfGuests(),
                        it.isEmpty()))
                .collect(toList());

        return new TableGroupRequest(createdDate, orderTableRequests);
    }

    private List<OrderTableRequest> toOrderTableRequests(final List<OrderTableResponse> orderTableResponses) {
        return orderTableResponses.stream()
                .map(it -> new OrderTableRequest(it.getId(), it.getTableGroupId(), it.getNumberOfGuests(), it.isEmpty()))
                .collect(toList());
    }

    private void saveCookingOrder(final OrderTableResponse createdOrderTable) {
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
        final Order order = new Order(createdOrderTable.getId(), "COOKING", LocalDateTime.now(),
                List.of(orderLineItem));
        orderDao.save(order);
    }

    private List<OrderTable> toOrderTables(final List<OrderTableResponse> orderTableResponses) {
        return orderTableResponses.stream()
                .map(it -> new OrderTable(it.getId(), it.getTableGroupId(), it.getNumberOfGuests(), it.isEmpty()))
                .collect(toList());
    }
}
