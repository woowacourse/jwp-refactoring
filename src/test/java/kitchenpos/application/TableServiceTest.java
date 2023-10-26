package kitchenpos.application;

import java.util.List;
import kitchenpos.Money;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.application.ChangeNumberOfGuestsCommand;
import kitchenpos.table.application.ChangeTableEmptyCommand;
import kitchenpos.table.application.CreateTableCommand;
import kitchenpos.table.application.OrderTableDto;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class TableServiceTest extends ServiceTest {


    @Autowired
    private TableService tableService;


    @Test
    void 테이블을_생성할_수_있다() {
        //given
        CreateTableCommand 테이블_생성_요청 = new CreateTableCommand();

        //when
        OrderTableDto 생성된_주문 = tableService.create(테이블_생성_요청);

        //then
        assertThat(생성된_주문.getId()).isNotNull();
    }

    @Test
    void 테이블_목록을_조회할_수_있다() {
        //given
        OrderTable 생성된_테이블 = orderTableRepository.save(new OrderTable(2, false));

        //when
        List<OrderTable> actual = tableService.list();

        //then
        assertThat(actual)
                .extracting(OrderTable::getId)
                .contains(생성된_테이블.getId());
    }

    @Nested
    class 테이블_인원_변경 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = empty가_아닌_테이블_조회();
            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 9);

            //when
            OrderTableDto 변경된_테이블 = tableService.changeNumberOfGuests(커맨드);

            //then
            assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(9);
        }

        private OrderTable empty가_아닌_테이블_조회() {
            OrderTable 테이블 = new OrderTable(2, false);
            return orderTableRepository.save(테이블);
        }

        @Test
        void 인원이_음수면_예외가_발생한다() {
            //given
            OrderTable 테이블 = orderTableRepository.save(new OrderTable(2, false));
            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), -1);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_존재하지_않으면_예외가_발생한다() {
            //given
            OrderTable 테이블_엔티티 = new OrderTable(2, false);
            OrderTable 테이블 = orderTableRepository.save(테이블_엔티티);
            orderTableRepository.delete(테이블);

            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 1);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블이_empty이면_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable(0, true);
            orderTableRepository.save(테이블);

            ChangeNumberOfGuestsCommand 커맨드 = new ChangeNumberOfGuestsCommand(테이블.getId(), 9);

            //expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(커맨드))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class 테이블_비우기 {

        @Test
        void 성공() {
            //given
            OrderTable 테이블 = new OrderTable(2, false);
            OrderTable 생성된_테이블 = orderTableRepository.save(테이블);
            ChangeTableEmptyCommand 테이블_비우기_요청 = new ChangeTableEmptyCommand(생성된_테이블.getId(), true);

            //when
            OrderTableDto 변경된_테이블 = tableService.changeEmpty(테이블_비우기_요청);

            //then
            assertThat(변경된_테이블.isEmpty()).isTrue();
        }

        @Test
        void 테이블에_COMPLETION_상태가_아닌_주문이_있으면_예외가_발생한다() {
            //given
            OrderTable 테이블 = new OrderTable(2, false);
            OrderTable 생성된_테이블 = orderTableRepository.save(테이블);
            주문만들기(생성된_테이블);
            ChangeTableEmptyCommand 테이블_비우기_요청 = new ChangeTableEmptyCommand(생성된_테이블.getId(), true);

            //expect
            assertThatThrownBy(() -> tableService.changeEmpty(테이블_비우기_요청))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private void 주문만들기(final OrderTable 생성된_테이블) {
            OrderLineItem 주문상품 = 주문_상품_만들기();
            Order 주문 = new Order(생성된_테이블.getId(), List.of(주문상품));
            주문.startCooking();
            orderRepository.save(주문);
        }

        private OrderLineItem 주문_상품_만들기() {
            final var 메뉴 = menuRepository.findAll().get(0);
            return OrderLineItem.of(null, 메뉴.getId(), "메뉴이름", Money.of(10_000), 1);
        }

        @Test
        void 그룹에속한_테이블이면_예외가_발생한다() {
            //given
            OrderTable 그룹있는_테이블 = orderTableRepository.save(new OrderTable(0, true));
            TableGroup 생성된_그룹 = 그룹_생성하기(그룹있는_테이블);
            그룹있는_테이블.changeTableGroup(생성된_그룹.getId());
            orderTableRepository.save(그룹있는_테이블);

            //expect
            ChangeTableEmptyCommand 테이블_비우기_요청 = new ChangeTableEmptyCommand(그룹있는_테이블.getId(), true);
            assertThatThrownBy(() -> tableService.changeEmpty(테이블_비우기_요청))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private TableGroup 그룹_생성하기(final OrderTable 포함할_테이블) {
            List<OrderTable> 테이블_목록 = List.of(포함할_테이블, new OrderTable(0, true));
            TableGroup 테이블_그룹 = new TableGroup();
            tableGroupRepository.save(테이블_그룹);
            테이블_그룹.changeOrderTables(테이블_목록);
            return 테이블_그룹;
        }

    }

}
