package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.application.dto.OrderTableIdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블이 비었을 때, 예외를 발생한다")
    @Test
    void not_empty_table_exception() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 단체 지정은 최소 2개 이상의 주문 테이블이 있어야 합니다.");
    }

    @DisplayName("두 개 미만의 테이블일 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void not_enough_count_of_table_exception() {
        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupCreateRequest(Collections.singletonList(new OrderTableIdRequest(1L)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 단체 지정은 최소 2개 이상의 주문 테이블이 있어야 합니다.");
    }

    @DisplayName("주문 테이블이 정보가 올바르지 않을 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void order_table_not_found_exception() {
        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupCreateRequest(Arrays.asList(new OrderTableIdRequest(0L), new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 올바르지 않습니다.");
    }

    @DisplayName("주문 테이블이 이미 단체로 지정되었을 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void already_set_group_exception() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(
                new TableGroup(Arrays.asList(new OrderTable(10, false), new OrderTable(10, false))));

        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupCreateRequest(Arrays.asList(new OrderTableIdRequest(orderTable1.getId()),
                        new OrderTableIdRequest(orderTable2.getId())))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블 또는 이미 그룹 지정이 되어있습니다.");
    }

    @DisplayName("주문 테이블이 비어있을 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void empty_order_table_exception() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(
                new TableGroup(Arrays.asList(new OrderTable(10, false), new OrderTable(10, false))));

        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, true));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(tableGroup.getId(), 10, false));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(
                new TableGroupCreateRequest(Arrays.asList(new OrderTableIdRequest(orderTable1.getId()),
                        new OrderTableIdRequest(orderTable2.getId())))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블 또는 이미 그룹 지정이 되어있습니다.");
    }

    @DisplayName("테이블을 단체로 등록할 수 있다")
    @Test
    void create() {
        final TableGroupResponse response = tableGroupService.create(
                new TableGroupCreateRequest(Arrays.asList(new OrderTableIdRequest(1L),
                        new OrderTableIdRequest(2L))));

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("테이블 주문 상태가 COOKING 또는 MEAL일 때 단체를 해제하면, 예외를 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void not_completion_exception(final String status) {
        // given
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, false));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, false));

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(Arrays.asList(orderTable1, orderTable2)));

        orderDao.save(new Order(orderTable1.getId(), status, LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));
        orderDao.save(new Order(orderTable2.getId(), status, LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리 또는 식사 중 일 때, 단체 지정을 해제할 수 없습니다.");
    }

    @DisplayName("테이블 단체 지정을 해제한다")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, false));
        final OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, false));

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(Arrays.asList(orderTable1, orderTable2)));

        orderDao.save(new Order(orderTable1.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));
        orderDao.save(new Order(orderTable2.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L))));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }
}
