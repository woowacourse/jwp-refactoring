package kitchenpos.ordertable.application;

import static kitchenpos.fixture.TableFixture.EMPTY_TABLE;
import static kitchenpos.fixture.TableFixture.createTableByEmpty;
import static kitchenpos.fixture.TableFixture.createTableById;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.OrderTableEmptyChangeRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsChangeRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.exception.OrderTableUpdateException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void create_메서드는_주문_테이블을_생성한다() {
        // when
        final OrderTable createdOrderTable = tableService.create(new OrderTableRequest(null, 0, true));

        // then
        assertThat(createdOrderTable)
                .usingRecursiveComparison()
                .isEqualTo(new OrderTable(createdOrderTable.getId(), null, 0, true));
    }

    @Test
    void list_메서드는_모든_주문_테이블을_조회한다() {
        // given
        final OrderTable orderTable = orderTableRepository.save(EMPTY_TABLE);

        // when
        final List<OrderTable> tables = tableService.list();

        // then
        assertThat(tables)
                .usingRecursiveComparison()
                .isEqualTo(List.of(orderTable));
    }

    @Nested
    class changeEmpty_메서드는 {

        @Test
        void 주문_테이블의_주문_가능_상태를_바꾼다() {
            // given
            final OrderTable createdOrderTable = tableService.create(new OrderTableRequest(null, 0, true));

            // when
            final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);
            final OrderTable changedOrderTable = tableService.changeEmpty(createdOrderTable.getId(), request);

            // then
            assertThat(changedOrderTable.isEmpty()).isFalse();
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final long nonExistTableId = 99L;
            final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(nonExistTableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_그룹에_속해있으면_예외가_발생한다() {
            // given
            final OrderTable orderTable1 = orderTableRepository.save(EMPTY_TABLE);
            final OrderTable orderTable2 = orderTableRepository.save(EMPTY_TABLE);
            final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));
            tableGroupRepository.save(tableGroup);

            final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_주문_상태가_요리중이라면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(EMPTY_TABLE);
            final Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1)));
            orderRepository.save(order);
            final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), request))
                    .isInstanceOf(OrderTableUpdateException.class);
        }

        @Test
        void 주문_테이블의_주문_상태가_식사중이라면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(EMPTY_TABLE);
            final Order order = new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                    List.of(new OrderLineItem(null, 1L, 1)));
            orderRepository.save(order);
            final OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(OrderTableUpdateException.class);
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Test
        void 주문_테이블의_손님수를_변경한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(createTableById(null));

            // when
            final OrderTableNumberOfGuestsChangeRequest request = new OrderTableNumberOfGuestsChangeRequest(10);
            final OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), request);

            // then
            assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(10);
        }

        @Test
        void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
            // given
            final long nonExistId = 99L;

            // when & then
            final OrderTableNumberOfGuestsChangeRequest request = new OrderTableNumberOfGuestsChangeRequest(10);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonExistId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_주문_불가능한_상태이면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(createTableByEmpty(true));

            // when & then
            final OrderTableNumberOfGuestsChangeRequest request = new OrderTableNumberOfGuestsChangeRequest(10);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
