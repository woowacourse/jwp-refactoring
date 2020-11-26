package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "classpath:/init-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequest
            = TestObjectFactory.createOrderTableCreateRequest(1, false);

        OrderTableResponse savedTable = tableService.create(orderTableCreateRequest);

        assertAll(() -> {
            assertThat(savedTable).isInstanceOf(OrderTableResponse.class);
            assertThat(savedTable.getId()).isNotNull();
            assertThat(savedTable.getNumberOfGuests()).isNotNull();
            assertThat(savedTable.getNumberOfGuests()).isEqualTo(1);
            assertThat(savedTable.getTableGroupId()).isNull();
            assertThat(savedTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        List<OrderTableResponse> tables = tableService.list();

        assertAll(() -> {
            assertThat(tables).isNotEmpty();
            assertThat(tables).hasSize(8);
        });
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTableCreateRequest oldOrderTableCreateRequest =
            TestObjectFactory.createOrderTableCreateRequest(1, false);
        OrderTableChangeRequest newOrderTableCreateRequest =
            TestObjectFactory.createOrderTableChangeRequest(1, true);

        OrderTableResponse savedTable = tableService.create(oldOrderTableCreateRequest);
        OrderTableResponse changeTable = tableService
            .changeEmpty(savedTable.getId(), newOrderTableCreateRequest);

        assertThat(changeTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다. - tableGroupId가 존재할 경우")
    @Test
    void changeEmpty_IfTableGroupIdIsNull_ThrowException() {
        OrderTableCreateRequest oldOrderTableCreateRequest =
            TestObjectFactory.createOrderTableCreateRequest(1, true);
        OrderTableCreateRequest newOrderTableCreateRequest =
            TestObjectFactory.createOrderTableCreateRequest(2, true);

        OrderTableResponse savedOrderTableA = tableService.create(oldOrderTableCreateRequest);
        OrderTableResponse savedOrderTableB = tableService.create(newOrderTableCreateRequest);
        List<OrderTableResponse> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroupCreateRequest tableGroupCreateRequest = TestObjectFactory
            .createTableGroupCreateRequest(orderTables);
        tableGroupService.create(tableGroupCreateRequest);

        OrderTableChangeRequest newOrderTable
            = TestObjectFactory.createOrderTableChangeRequest(2, true);
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableA.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다. - 주문 상태가 요리, 식사인 경우")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_IfStatusIsCookingOrMeal_ThrowException(String status) {
        OrderTableCreateRequest oldOrderTableCreateRequest =
            TestObjectFactory.createOrderTableCreateRequest(1, false);
        OrderTableChangeRequest newOrderTableCreateRequest =
            TestObjectFactory.createOrderTableChangeRequest(1, true);

        OrderTableResponse savedTable = tableService.create(oldOrderTableCreateRequest);

        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        OrderCreateRequest orderCreateRequest = TestObjectFactory
            .createOrderCreateRequest(savedTable.toEntity(null),
                orderStatus.name(),
                orderLineItems);
        orderService.create(orderCreateRequest);

        assertThatThrownBy(
            () -> tableService.changeEmpty(savedTable.getId(), newOrderTableCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableCreateRequest oldOrderTableCreateRequest
            = TestObjectFactory.createOrderTableCreateRequest(1, false);
        OrderTableChangeRequest newOrderTableCreateRequest
            = TestObjectFactory.createOrderTableChangeRequest(2, false);
        OrderTableResponse savedTable = tableService.create(oldOrderTableCreateRequest);

        OrderTableResponse changedSavedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), newOrderTableCreateRequest);

        assertThat(changedSavedTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("손님 수를 변경한다. - 손님의 수가 0 이하인 경우")
    @Test
    void changeNumberOfGuests_IfGuestIsNotPositive_Throw_Exception() {
        assertThatThrownBy(
            () -> TestObjectFactory.createOrderTableChangeRequest(-1, false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다. - 테이블이 비어있을 경우")
    @Test
    void changeNumberOfGuests_IfTableIsEmpty_ThrowException() {
        OrderTableCreateRequest oldOrderTableCreateRequest
            = TestObjectFactory.createOrderTableCreateRequest(1, true);
        OrderTableChangeRequest newOrderTableCreateRequest
            = TestObjectFactory.createOrderTableChangeRequest(2, true);
        OrderTableResponse savedTable = tableService.create(oldOrderTableCreateRequest);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTableCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다. - 주문 테이블이 존재하지 않는 경우")
    @Test
    void changeNumberOfGuests_IfOrderedTableNotExist_ThrowException() {
        OrderTableChangeRequest newOrderTableCreateRequest
            = TestObjectFactory.createOrderTableChangeRequest(2, true);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(0L, newOrderTableCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}