package kitchenpos.application;

import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static kitchenpos.DomainFixtures.빈_주문_테이블_4인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문_테이블을_그룹화할_수_있다() {
        List<OrderTable> 주문_테이블 = 주문_테이블들(true, true);
        TableGroup 단체_지정 = 단체_지정(주문_테이블);

        assertAll(
                () -> assertThat(단체_지정.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(단체_지정.getId()).isNotNull()
        );
    }

    @Test
    void 주문_테이블이_1개_이하이면_예외가_발생한다() {
        OrderTable 테이블1 = tableService.create(빈_주문_테이블_3인());
        List<OrderTable> 주문_테이블  = Arrays.asList(테이블1);

        final List<Long> 주문_테이블_ID = 주문_테이블.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), 주문_테이블_ID))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_존재하지_않으면_예외가_발생한다() {
        List<Long> 주문_테이블 = new ArrayList<>();

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), 주문_테이블))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문과_존재하는_테이블의_수가_같지_않으면_예외가_발생한다() {
        OrderTable 테이블1 = tableService.create(빈_주문_테이블_3인());
        List<OrderTable> 주문_테이블 = Arrays.asList(테이블1);
        tableService.create(빈_주문_테이블_4인());

        final List<Long> 주문_테이블_ID = 주문_테이블.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), 주문_테이블_ID))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        List<OrderTable> orderTables = 주문_테이블들(false, true);

        final List<Long> 주문_테이블_ID = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), 주문_테이블_ID))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        List<OrderTable> orderTables = 주문_테이블들(true, true);
        TableGroup tableGroup = 단체_지정(orderTables);

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = tableService.list();

        assertAll(
                () -> assertThat(tables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(tables.get(1).getTableGroupId()).isNull()
        );
    }

    @Test
    void 단체를_해제하려는_테이블의_상태가_식사_중이거나_요리_중이면_예외가_발생한다() {
        List<OrderTable> orderTables = 주문_테이블들(false, false);

        Order 요리중_주문 = new Order(orderTables.get(0).getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        List<OrderLineItemRequest> orderLineItemRequests = 주문_항목_요청을_생성한다(요리중_주문);

        orderTables.get(0).setEmpty(true);
        orderTables.get(1).setEmpty(true);
        TableGroup 단체_테이블 = 단체_지정(orderTables);
        orderService.create(new OrderRequest(요리중_주문.getOrderTableId(), 요리중_주문.getOrderStatus(),
                요리중_주문.getOrderedTime(), orderLineItemRequests));

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_테이블.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup 단체_지정(List<OrderTable> orderTables) {
        final List<Long> 주문_테이블_ID = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        TableGroup tableGroup = tableGroupService.create(new TableGroupRequest(LocalDateTime.now(), 주문_테이블_ID));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());
        return tableGroup;
    }
}
