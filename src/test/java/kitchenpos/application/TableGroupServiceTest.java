package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "classpath:/init-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequestA
            = TestObjectFactory.createOrderTableCreateRequest(1, true);
        OrderTableCreateRequest orderTableCreateRequestB
            = TestObjectFactory.createOrderTableCreateRequest(2, true);
        OrderTableResponse savedOrderTableA
            = tableService.create(orderTableCreateRequestA);
        OrderTableResponse savedOrderTableB
            = tableService.create(orderTableCreateRequestB);
        List<OrderTableResponse> orderTableResponses
            = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroupCreateRequest tableGroupCreateRequest
            = TestObjectFactory.createTableGroupCreateRequest(orderTableResponses);
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

        assertAll(() -> {
            assertThat(tableGroupResponse).isInstanceOf(TableGroupResponse.class);
            assertThat(tableGroupResponse.getId()).isNotNull();
            assertThat(tableGroupResponse.getCreatedDate()).isNotNull();
            assertThat(tableGroupResponse.getOrderTables()).isNotNull();
            assertThat(tableGroupResponse.getOrderTables()).isNotEmpty();
            assertThat(tableGroupResponse.getOrderTables()).hasSize(2);
        });
    }

    @DisplayName("테이블 그룹을 생성한다. - 테이블이 비어있지 않을 경우")
    @Test
    void create_IfTableNotEmpty_ThrowException() {
        OrderTableCreateRequest orderTableCreateRequestA
            = TestObjectFactory.createOrderTableCreateRequest(1, false);
        OrderTableCreateRequest orderTableCreateRequestB
            = TestObjectFactory.createOrderTableCreateRequest(2, false);
        OrderTableResponse savedOrderTableA
            = tableService.create(orderTableCreateRequestA);
        OrderTableResponse savedOrderTableB
            = tableService.create(orderTableCreateRequestB);
        List<OrderTableResponse> orderTableResponses
            = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroupCreateRequest tableGroupCreateRequest
            = TestObjectFactory.createTableGroupCreateRequest(orderTableResponses);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성한다. - 테이블 갯수가 2 미만일 경우")
    @Test
    void create_IfTableIsLessThanTwo_ThrowException() {
        OrderTableCreateRequest orderTableCreateRequest
            = TestObjectFactory.createOrderTableCreateRequest(1, true);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);
        List<OrderTableResponse> orderTables = Collections.singletonList(orderTableResponse);

        TableGroupCreateRequest tableGroupCreateRequest = TestObjectFactory
            .createTableGroupCreateRequest(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTableCreateRequest orderTableCreateRequestA
            = TestObjectFactory.createOrderTableCreateRequest(1, true);
        OrderTableCreateRequest orderTableCreateRequestB
            = TestObjectFactory.createOrderTableCreateRequest(2, true);
        OrderTableResponse savedOrderTableA
            = tableService.create(orderTableCreateRequestA);
        OrderTableResponse savedOrderTableB
            = tableService.create(orderTableCreateRequestB);
        List<OrderTableResponse> orderTableResponses
            = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroupCreateRequest tableGroupCreateRequest
            = TestObjectFactory.createTableGroupCreateRequest(orderTableResponses);
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupCreateRequest);
        tableGroupService.ungroup(savedTableGroup.getId());

        Optional<OrderTable> ungroupedOrderTableA = orderTableRepository
            .findById(savedOrderTableA.getId());
        Optional<OrderTable> ungroupedOrderTableB = orderTableRepository
            .findById(savedOrderTableB.getId());

        assertAll(() -> {
            assertThat(ungroupedOrderTableA).isNotEqualTo(Optional.empty());
            assertThat(ungroupedOrderTableA.get()
                .getTableGroup()).isNull();
            assertThat(ungroupedOrderTableA.get()
                .isEmpty()).isFalse();
            assertThat(ungroupedOrderTableB).isNotEqualTo(Optional.empty());
            assertThat(ungroupedOrderTableB.get()
                .getTableGroup()).isNull();
            assertThat(ungroupedOrderTableB.get()
                .isEmpty()).isFalse();
        });
    }

    @DisplayName("테이블 그룹을 해제한다. - 주문이 Cooking, Meal일 경우")
    @Test
    void ungroup_IfStatusIsCookingOrMeal_ThrowException() {
        OrderTableCreateRequest orderTableCreateRequestA
            = TestObjectFactory.createOrderTableCreateRequest(1, false);
        OrderTableCreateRequest orderTableCreateRequestB
            = TestObjectFactory.createOrderTableCreateRequest(2, false);
        OrderTableResponse savedOrderTableA
            = tableService.create(orderTableCreateRequestA);
        OrderTableResponse savedOrderTableB
            = tableService.create(orderTableCreateRequestB);
        List<OrderTableResponse> orderTableResponses
            = Arrays.asList(savedOrderTableA, savedOrderTableB);

        OrderTableChangeRequest newOrderTableCreateRequest =
            TestObjectFactory.createOrderTableChangeRequest(1, true);
        tableService.changeEmpty(savedOrderTableA.getId(), newOrderTableCreateRequest);
        tableService.changeEmpty(savedOrderTableB.getId(), newOrderTableCreateRequest);

        TableGroupCreateRequest tableGroupCreateRequest
            = TestObjectFactory.createTableGroupCreateRequest(orderTableResponses);
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupCreateRequest);

        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        TableGroup tableGroup = tableGroupRepository.getOne(savedOrderTableA.getId());
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(savedOrderTableA.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        orderService.create(orderCreateRequest);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
