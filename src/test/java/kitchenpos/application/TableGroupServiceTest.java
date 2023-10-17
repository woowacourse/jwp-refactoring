package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성할_수_있다() {
            //given
            TableGroup 테이블그룹 = new TableGroup();
            테이블그룹.setOrderTables(List.of(테이블_생성(), 테이블_생성()));

            //when
            TableGroup 생성된_테이블그룹 = tableGroupService.create(테이블그룹);

            //then
            assertThat(생성된_테이블그룹.getId()).isNotNull();
        }

        @Test
        void 테이블이_2개_미만이면_예외가_발생한다() {
            //given
            TableGroup 테이블그룹 = new TableGroup();
            테이블그룹.setOrderTables(emptyList());

            //expect
            assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            //given
            TableGroup 테이블그룹 = new TableGroup();
            테이블그룹.setOrderTables(List.of(테이블_생성(), new OrderTable()));

            //expect
            assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_그룹에_속한_테이블이_있으면_에외가_발생한다() {
            //given
            OrderTable 테이블 = 테이블_생성();
            TableGroup 그룹 = new TableGroup();
            그룹.setOrderTables(List.of(테이블, 테이블_생성()));
            tableGroupService.create(그룹);

            TableGroup 새_그룹 = new TableGroup();
            새_그룹.setOrderTables(List.of(테이블, 테이블_생성()));

            //expect
            assertThatThrownBy(() -> tableGroupService.create(새_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    private OrderTable 테이블_생성() {
        final var 테이블 = new OrderTable();
        테이블.setEmpty(true);
        테이블.changeNumberOfGuests(4);
        return orderTableDao.save(테이블);
    }

    @Nested
    class 테이블_그룹_해제 {
        @Test
        void 성공() {
            //given
            TableGroup 테이블_그룹 = new TableGroup();
            OrderTable 테이블 = 테이블_생성();
            Order 주문 = new Order();
            주문.setOrderTableId(테이블.getId());
            주문.setOrderStatus(OrderStatus.COMPLETION.name());
            주문.setOrderedTime(now());
            orderDao.save(주문);
            테이블_그룹.setOrderTables(List.of(테이블, 테이블_생성()));
            TableGroup 생성된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            //when
            tableGroupService.ungroup(생성된_테이블_그룹.getId());

            //then
            assertThat(orderTableDao.findById(테이블.getId()).get().getTableGroupId()).isNull();
        }

        @Test
        void COMPLETION이_아닌_주문이_있으면_예외가_발생한다() {
            //given
            TableGroup 테이블_그룹 = new TableGroup();
            OrderTable 테이블 = 테이블_생성();
            Order 주문 = new Order();
            주문.setOrderTableId(테이블.getId());
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderedTime(now());
            orderDao.save(주문);
            테이블_그룹.setOrderTables(List.of(테이블, 테이블_생성()));
            TableGroup 생성된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            //expect
            assertThatThrownBy(() -> tableGroupService.ungroup(생성된_테이블_그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

}
