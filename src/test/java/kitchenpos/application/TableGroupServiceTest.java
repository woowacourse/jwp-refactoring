package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableGroupService service;

    private TableGroup tableGroup;
    private Order order;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void setUp() {
        OrderLineItem orderItem = new OrderLineItem();
        orderItem.setSeq(2L);
        orderItem.setQuantity(2);
        orderItem.setOrderId(1L);
        orderItem.setMenuId(1L);

        order = new Order();
        order.setId(1L);
        order.setOrderLineItems(Lists.newArrayList(orderItem));
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(7L);

        orderTable1 = new OrderTable();
        orderTable1.setId(7L);
        orderTable1.setNumberOfGuests(4);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(8L);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(true);

        tableGroup = new TableGroup();
        tableGroup.setId(20L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Lists.newArrayList(orderTable1, orderTable2));
    }

    @DisplayName("단체 지정 실패 - 주문 테이블이 없을 경우")
    @Test
    public void createFailZeroTable() {
        tableGroup.setOrderTables(Lists.newArrayList());

        assertThatThrownBy(() -> service.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 실패 - 주문 테이블 갯수가 1개일 경우")
    @Test
    public void createFailSingleTable() {
        tableGroup.setOrderTables(Lists.newArrayList(orderTable1));

        assertThatThrownBy(() -> service.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 실패 - 지정하고자 하는 테이블 갯수와 저장가능한 테이블 갯수가 다를 경우")
    @Test
    public void createFailNotMatchTableCount() {
        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList());

        assertThatThrownBy(() -> service.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 실패 - 이미 지정된 테이블 단체가 있을 경우")
    @Test
    public void createFailExistedTableGroup() {
        orderTable1.setTableGroupId(1L);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList(orderTable1, orderTable2));

        assertThatThrownBy(() -> service.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정")
    @Test
    public void createTableGroup() {
        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        final TableGroup savedTableGroup = service.create(tableGroup);

        assertThat(savedTableGroup.getId()).isEqualTo(20L);
        assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("단체 해제 실패 - 주문 상태가 조리 중, 식사 중일 경우")
    @Test
    public void unGroupFail() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> service.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제")
    @Test
    public void unGroup() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Lists.newArrayList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        service.ungroup(tableGroup.getId());

        assertThat(orderTable1.isEmpty()).isFalse();
        assertThat(orderTable2.isEmpty()).isFalse();
    }
}