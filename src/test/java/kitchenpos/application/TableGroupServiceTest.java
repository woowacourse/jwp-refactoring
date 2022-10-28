package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable orderTable3;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableDao.save(new OrderTable(0, true));
        orderTable2 = orderTableDao.save(new OrderTable(0, true));
        orderTable3 = orderTableDao.save(new OrderTable(0, true));
    }

    @DisplayName("단체 지정을 생성한다. - 주문 테이블은 2개 이상이어야 한다")
    @Test
    void create() {
        List<OrderTableRequest> requests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId()));
        TableGroupResponse actual = tableGroupService.create(new TableGroupRequest(requests));

        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("주문 테이블이 2개보다 작으면 예외가 발생한다.")
    @Test
    void createFailureWhenTableLessThanTwo() {

        assertThatThrownBy(
                () -> tableGroupService.create(
                        new TableGroupRequest(List.of(new OrderTableRequest(orderTable1.getId()))))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenTableIsEmpty() {

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Collections.emptyList()))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블이 비어있지 않으면(empty is false) 예외가 발생한다.")
    @Test
    void createFailureWhenSavedOrderTableExists() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(1, false));

        List<OrderTableRequest> requests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId()));
        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(requests))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블의 테이블 그룹 아이디가 있으면 예외가 발생한다.")
    @Test
    void createFailureWhenGroupIdExists() {
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 1, true));

        List<OrderTableRequest> requests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId()));
        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(requests))
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        List<OrderTableRequest> requests = List.of(
                new OrderTableRequest(orderTable1.getId()),
                new OrderTableRequest(orderTable2.getId()),
                new OrderTableRequest(orderTable3.getId()));

        TableGroupResponse actual = tableGroupService.create(new TableGroupRequest(requests));

        tableGroupService.ungroup(actual.getId());
    }

    @DisplayName("단체 지정을 해제할 때, 주문 상태가 COOKING이나 MEAL 이면 예외가 발생한다.")
    @MethodSource
    @ParameterizedTest
    void ungroupFailureWhenOrderStatusIsCookingOrMeal(OrderStatus orderStatus) {
        orderDao.save(new Order(orderTable1.getId(), orderStatus, LocalDateTime.now()));
        orderDao.save(new Order(orderTable2.getId(), orderStatus, LocalDateTime.now()));

        TableGroup tableGroup = tableGroupDao.save(
                new TableGroup(1L, LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        assertThatThrownBy(
                () -> tableGroupService.ungroup(tableGroup.getId())
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<OrderStatus> ungroupFailureWhenOrderStatusIsCookingOrMeal() {
        return Stream.of(OrderStatus.COOKING, OrderStatus.MEAL);
    }
}
