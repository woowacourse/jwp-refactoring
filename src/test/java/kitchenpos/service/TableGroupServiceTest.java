package kitchenpos.service;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.util.FakeOrderDao;
import kitchenpos.util.FakeOrderTableDao;
import kitchenpos.util.FakeTableGroupDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupServiceTest {

    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final TableGroupDao tableGroupDao = new FakeTableGroupDao();
    private final TableGroupService tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() {
        preprocessWhenCreate(
                List.of(
                        new OrderTable(1L, null, 3, true),
                        new OrderTable(8L, null, 4, true)
                ));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L), new OrderTableRequest(8L))
        );

        TableGroup tableGroup = tableGroupService.create(tableGroupCreateRequest);

        assertAll(
                () -> assertThat(tableGroup.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(tableGroup.getId()),
                () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroupId()).isEqualTo(tableGroup.getId())
        );
    }
    @DisplayName("테이블이 없는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableEmpty() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("테이블이 하나만 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableOne() {
        preprocessWhenCreate(
                List.of(new OrderTable(1L, null, 3, true)));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(new OrderTableRequest(1L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("존재하지 않는 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableNotExist() {
        preprocessWhenCreate(
                List.of(
                        new OrderTable(1L, null, 3, true),
                        new OrderTable(6L, null, 4, true)));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(6L),
                        new OrderTableRequest(100L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("주문을 할 수 없는 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableNotEmpty() {
        preprocessWhenCreate(
                List.of(
                        new OrderTable(1L, null, 3, false),
                        new OrderTable(2L, null, 4, true)));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(2L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("이미 테이블 그룹에 묶인 테이블이 있는 테이블 그룹을 생성할 수 없다")
    @Test
    void create_orderTableAlreadyGrouped() {
        preprocessWhenCreate(
                List.of(
                        new OrderTable(1L, null, 3, true),
                        new OrderTable(4L, 1L, 4, true)));
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(new OrderTableRequest(1L),
                        new OrderTableRequest(4L)));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest));
    }
    @DisplayName("테이블 그룹 지정을 해제한다")
    @Test
    void ungroup() {
        preprocessWhenUngroup(new TableGroup(LocalDateTime.now()),
                List.of(new OrderTable(1L, 1L, 4, false),
                        new OrderTable(2L, 1L, 4, false)),
                List.of(new Order(1L, OrderStatus.COMPLETION, LocalDateTime.now())));
        tableGroupService.ungroup(1L);

        assertThat(orderTableDao.findAllByTableGroupId(1L).isEmpty()).isTrue();
    }
    @DisplayName("주문이 진행중인 테이블이 있는 테이블 그룹 지정을 해제할 수 없다")
    @Test
    void ungroup_orderTableOrderComplete() {
        preprocessWhenUngroup(new TableGroup(LocalDateTime.now()),
                List.of(new OrderTable(1L, 1L, 4, false)),
                List.of(new Order(1L, OrderStatus.COOKING, LocalDateTime.now())));
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    private void preprocessWhenCreate(List<OrderTable> orderTables) {
        orderTables.forEach(orderTableDao::save);
    }

    private void preprocessWhenUngroup(TableGroup tableGroup, List<OrderTable> orderTables, List<Order> orders) {
        tableGroupDao.save(tableGroup);
        orderTables.forEach(orderTableDao::save);
        orders.forEach(orderDao::save);
    }
}
