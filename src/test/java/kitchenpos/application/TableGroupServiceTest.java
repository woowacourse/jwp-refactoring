package kitchenpos.application;

import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static kitchenpos.DomainFixtures.빈_주문_테이블_4인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
        List<OrderTable> 주문_테이블 = new ArrayList<>();
        OrderTable 테이블1 = tableService.create(빈_주문_테이블_3인());
        주문_테이블.add(테이블1);

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), 주문_테이블))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_존재하지_않으면_예외가_발생한다() {
        List<OrderTable> 주문_테이블 = new ArrayList<>();

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), 주문_테이블))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문과_존재하는_테이블의_수가_같지_않으면_예외가_발생한다() {
        OrderTable 테이블1 = tableService.create(빈_주문_테이블_3인());
        List<OrderTable> 주문_테이블 = new ArrayList<>();
        주문_테이블.add(테이블1);
        tableService.create(빈_주문_테이블_4인());

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), 주문_테이블))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        List<OrderTable> orderTables = 주문_테이블들(false, true);

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables))
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
        List<OrderTable> orderTables = 주문_테이블들(true, true);

        Order 요리중_주문 = new Order(orderTables.get(0).getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(요리중_주문);

        TableGroup 단체_테이블 = 단체_지정(orderTables);
        orderService.create(요리중_주문);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_테이블.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup 단체_지정(List<OrderTable> orderTables) {
        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), orderTables));
        orderTables.get(0).setTableGroupId(tableGroup.getId());
        orderTables.get(1).setTableGroupId(tableGroup.getId());
        return tableGroup;
    }
}
