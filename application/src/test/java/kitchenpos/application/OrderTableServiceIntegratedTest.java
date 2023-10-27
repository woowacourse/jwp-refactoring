package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order_table.application.OrderTableService;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.OrderTableGroup;
import kitchenpos.order_table.domain.OrderTables;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import kitchenpos.order_table.domain.repository.TableGroupRepository;
import kitchenpos.order_table.dto.response.OrderTableResponse;
import kitchenpos.execute.ServiceIntegrateTest;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static java.time.LocalDateTime.now;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTableServiceIntegratedTest extends ServiceIntegrateTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class 주문_테이블을_저장한다 {

        private OrderTableGroup orderTableGroup;
        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            OrderTable orderTable1 = OrderTableFixture.주문_테이블_생성();
            OrderTable orderTable2 = OrderTableFixture.주문_테이블_생성();
            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);
            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            OrderTableGroup orderTableGroup = new OrderTableGroup(orderTables, now());
            this.orderTableGroup = tableGroupRepository.save(orderTableGroup);
            this.orderTable1 = orderTable1;
            this.orderTable2 = orderTable2;
        }

        @Test
        void 주문_테이블을_저장한다() {
            // when, then
            assertDoesNotThrow(() -> orderTableService.create(
                    3,
                    true
            ));
        }

    }

    @Nested
    class 주문_목록을_반환한다 {

        @BeforeEach
        void setUp() {
            OrderTable orderTable1 = OrderTableFixture.주문_테이블_생성();
            OrderTable orderTable2 = OrderTableFixture.주문_테이블_생성();
            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            OrderTableGroup orderTableGroup = new OrderTableGroup(orderTables, now());
            OrderTableGroup savedOrderTableGroup = tableGroupRepository.save(orderTableGroup);

            orderTable1.updateTableGroup(savedOrderTableGroup);
            orderTable2.updateTableGroup(savedOrderTableGroup);
            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);
        }

        @Test
        void 주문_목록을_반환한다() {
            // when
            List<OrderTableResponse> orderTables = orderTableService.findAll();

            // then
            assertThat(orderTables).hasSize(2);
        }

    }

    @Nested
    class 주문_목록을_empty로_변경한다 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = OrderTableFixture.주문_테이블_생성();
            this.orderTable = orderTableRepository.save(orderTable);
        }

        @Test
        void 주문_목록을_empty로_변경한다() {
            // when
            orderTableService.changeIsEmpty(orderTable.getId(), true);
            OrderTable orderTable = orderTableRepository.getById(this.orderTable.getId());

            // then
            assertThat(orderTable.isEmpty()).isEqualTo(true);
        }

        @Test
        void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
            // when
            assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> orderTableService.changeIsEmpty(orderTable.getId() + 1L, true));
        }

    }

    @Nested
    class 손님_수를_변경한다 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = OrderTableFixture.주문_테이블_생성();
            this.orderTable = orderTableRepository.save(orderTable);
        }

        @Test
        void 손님_수를_변경한다() {
            // when
            orderTable.updateEmpty(false);
            orderTableRepository.save(orderTable);
            OrderTable savedOrderTable = orderTableService.changeNumberOfGuests(orderTable.getId(), 3);

            // then
            assertThat(savedOrderTable).isEqualTo(orderTable);
        }

        @Test
        void 주문_테이블의_손님_수가_0보다_작다면_예외가_발생한다() {
            // when
            assertThrows(IllegalArgumentException.class,
                    () -> orderTableService.changeNumberOfGuests(orderTable.getId(), -1)
            );
        }

        @Test
        void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
            // when
            assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> orderTableService.changeNumberOfGuests(orderTable.getId() + 1, -1)
            );
        }

        @Test
        void 주문_테이블이_비어있다면_예외가_발생한다() {
            // given
            orderTable.updateEmpty(true);

            // when, then
            assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> orderTableService.changeNumberOfGuests(orderTable.getId() + 1, -1)
            );
        }

    }

}