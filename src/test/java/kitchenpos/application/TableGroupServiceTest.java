package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.CreateTableGroupCommand;
import kitchenpos.application.dto.CreateTableGroupCommand.TableInGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성할_수_있다() {
            //given
            CreateTableGroupCommand 커맨드 = new CreateTableGroupCommand(
                    List.of(new TableInGroup(빈_테이블_생성().getId()), new TableInGroup(빈_테이블_생성().getId())));

            //when
            TableGroup 생성된_테이블그룹 = tableGroupService.create(커맨드);

            //then
            assertThat(생성된_테이블그룹.getId()).isNotNull();
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            //given
            TableInGroup 존재하지_않는_테이블 = new TableInGroup(null);
            CreateTableGroupCommand 커맨드 = new CreateTableGroupCommand(
                    List.of(new TableInGroup(1L), 존재하지_않는_테이블));

            //expect
            assertThatThrownBy(() -> tableGroupService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_그룹에_속한_테이블이_있으면_에외가_발생한다() {
            //given
            OrderTable 테이블 = 그룹에_속한_테이블_생성();
            CreateTableGroupCommand 커맨드 = new CreateTableGroupCommand(
                    List.of(new TableInGroup(테이블.getId()), new TableInGroup(빈_테이블_생성().getId()))
            );

            //expect
            assertThatThrownBy(() -> tableGroupService.create(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private OrderTable 그룹에_속한_테이블_생성() {
            OrderTable 테이블 = 빈_테이블_생성();
            CreateTableGroupCommand 커맨드 = new CreateTableGroupCommand(
                    List.of(new TableInGroup(테이블.getId()), new TableInGroup(빈_테이블_생성().getId()))
            );

            tableGroupService.create(커맨드);
            return orderTableRepository.findById(테이블.getId()).get();
        }

    }

    private OrderTable 빈_테이블_생성() {
        final var 테이블 = new OrderTable(0, true);
        return orderTableRepository.save(테이블);
    }

    @Nested
    class 테이블_그룹_해제 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = 빈_테이블_생성();

            OrderLineItem 주문_항목 = new OrderLineItem(null, null, 1L, 1);
            Order 주문 = new Order(null, 테이블.getId(), COOKING, now(), List.of(주문_항목));
            orderRepository.save(주문);

            TableGroup 테이블_그룹 = new TableGroup(null, now(), List.of(테이블, 빈_테이블_생성()));
            TableGroup 생성된_테이블_그룹 = tableGroupDao.save(테이블_그룹);

            //when
            tableGroupService.ungroup(생성된_테이블_그룹.getId());

            //then
            assertThat(orderTableRepository.findById(테이블.getId()).get().getTableGroupId()).isNull();
        }

        @Test
        void COMPLETION이_아닌_주문이_있으면_예외가_발생한다() {
            //given
            OrderTable 테이블 = 빈_테이블_생성();

            OrderLineItem 주문_항목 = new OrderLineItem(null, null, 1L, 1);
            Order 주문 = new Order(null, 테이블.getId(), COOKING, now(), List.of(주문_항목));
            orderRepository.save(주문);

            TableGroup 테이블_그룹 = new TableGroup(null, now(), List.of(테이블, 빈_테이블_생성()));
            TableGroup 생성된_테이블_그룹 = tableGroupDao.save(테이블_그룹);

            // todo: table group repository 도입 후 제거하기
            테이블.setTableGroupId(생성된_테이블_그룹.getId());
            orderTableRepository.save(테이블);

            //expect
            assertThatThrownBy(() -> tableGroupService.ungroup(생성된_테이블_그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
