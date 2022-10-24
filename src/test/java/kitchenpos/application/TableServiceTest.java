package kitchenpos.application;

import static kitchenpos.DomainFixtures.빈_주문_테이블_3인;
import static kitchenpos.DomainFixtures.빈_주문_테이블_4인;
import static kitchenpos.DomainFixtures.주문_테이블_3인;
import static kitchenpos.DomainFixtures.주문_테이블_4인;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
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
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Test
    void 주문_테이블을_생성할_수_있다() {
        OrderTable orderTable = tableService.create(빈_주문_테이블_3인());

        assertAll(
                () -> assertThat(orderTable.getId()).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(orderTable.isEmpty()).isTrue()
        );
    }

    @Test
    void 주문_테이블을_조회할_수_있다() {
        tableService.create(빈_주문_테이블_3인());
        tableService.create(빈_주문_테이블_4인());

        List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(2),
                () -> assertThat(orderTables.get(0).getId()).isNotNull(),
                () -> assertThat(orderTables.get(1).getId()).isNotNull()
        );
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_수_있다() {
        OrderTable orderTable = tableService.create(빈_주문_테이블_3인());
        OrderTable targetTable = tableService.changeEmpty(orderTable.getId(), 주문_테이블_3인());

        assertThat(targetTable.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_존재하지_않으면_예외가_발생한다() {
        assertThatThrownBy(
                () -> tableService.changeEmpty(1L,
                        빈_주문_테이블_3인())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_단체_지정이_되어_있으면_예외가_발생한다() {
        List<OrderTable> 주문_테이블 = 주문_테이블들(true, true);

        TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), 주문_테이블));
        주문_테이블.get(0).setTableGroupId(tableGroup.getId());
        주문_테이블.get(1).setTableGroupId(tableGroup.getId());

        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_테이블.get(0).getId(), 빈_주문_테이블_3인())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경하려_할_때_주문_테이블이_식사중이거나_요리중이면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(주문_테이블_3인());
        Order 요리중_주문 = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());
        주문_항목을_추가한다(요리중_주문);

        orderService.create(요리중_주문);

        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), 빈_주문_테이블_3인())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_방문한_손님의_명_수를_수정할_수_있다() {
        OrderTable orderTable = tableService.create(주문_테이블_3인());

        OrderTable targetOrder = tableService.changeNumberOfGuests(orderTable.getId(), 주문_테이블_4인());

        assertAll(
                () -> assertThat(targetOrder.getId()).isNotNull(),
                () -> assertThat(targetOrder.getNumberOfGuests()).isEqualTo(4)
        );
    }

    @Test
    void 주문_테이블의_방문한_손님의_명_수를_0명보다_적게_수정하면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(주문_테이블_3인());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(),
                        new OrderTable(null, -1, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_명_수를_수정할_때_존재하지_않는_테이블이면_예외가_발생한다() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, 주문_테이블_3인())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_명_수를_수정할_때_주문_테이블이_비어있으면_예외가_발생한다() {
        OrderTable orderTable = tableService.create(빈_주문_테이블_3인());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), 빈_주문_테이블_3인())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
