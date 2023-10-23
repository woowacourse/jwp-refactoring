package kitchenpos.application;

import kitchenpos.application.test.ServiceIntegrateTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.Order;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.fixture.OrderFixture.주문_생성;
import static kitchenpos.domain.fixture.OrderTableFixture.주문_테이블_생성;
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

        private TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            OrderTable orderTable1 = 주문_테이블_생성();
            OrderTable orderTable2 = 주문_테이블_생성();
            orderTableRepository.save(orderTable1);
            orderTableRepository.save(orderTable2);
            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            TableGroup tableGroup = new TableGroup(orderTables, now());
            this.tableGroup = tableGroupRepository.save(tableGroup);
        }

        @Test
        void 주문_테이블을_저장한다() {
            // when, then
            assertDoesNotThrow(() -> orderTableService.create(tableGroup.getId(), 3));
        }

    }

    @Nested
    class 주문_목록을_반환한다 {

        @BeforeEach
        void setUp() {
            OrderTable orderTable1 = 주문_테이블_생성();
            OrderTable orderTable2 = 주문_테이블_생성();
            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            TableGroup tableGroup = new TableGroup(orderTables, now());
            TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

            orderTable1.updateTableGroup(savedTableGroup);
            orderTable2.updateTableGroup(savedTableGroup);
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
            OrderTable orderTable = 주문_테이블_생성();
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

        @Test
        void 주문_테이블의_tableGroupId가_존재한다면_예외가_발생한다() {
            // given
            OrderTables orderTables = new OrderTables(List.of(orderTable));
            TableGroup tableGroup = new TableGroup(orderTables, now());
            tableGroupRepository.save(tableGroup);
            orderTable.updateTableGroup(tableGroup);
            orderTableRepository.save(orderTable);

            // when
            assertThrows(IllegalArgumentException.class,
                    () -> orderTableService.changeIsEmpty(orderTable.getId(), true));
        }

        @Test
        void 주문_상태가_COOKING_또는_MEAL이라면_예외가_발생한다() {
            // given
            Order order = orderRepository.save(주문_생성(orderTable));
            order.updateOrderStatus(COOKING);

            // when
            assertThrows(IllegalArgumentException.class, () -> orderTableService.changeIsEmpty(orderTable.getId(), true));
        }

    }

    @Nested
    class 손님_수를_변경한다 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = 주문_테이블_생성();
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