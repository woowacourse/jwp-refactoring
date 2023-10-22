package kitchenpos.application;

import kitchenpos.application.test.ServiceIntegrateTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.fixture.OrderFixture.주문_생성;
import static kitchenpos.domain.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceIntegrateTest extends ServiceIntegrateTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    class 주문을_생성한다 {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = orderTableRepository.save(주문_테이블_생성());
        }

        @Test
        void 주문을_생성한다() {
            // when, then
            orderTable.updateEmpty(false);
            orderTableRepository.save(orderTable);
            assertDoesNotThrow(() -> orderService.create(orderTable.getId()));
        }

        @Test
        void 주문_테이블이_존재하지_않는다면_예외가_발생한다() {
            // when, then
            assertThrows(InvalidDataAccessApiUsageException.class,
                    () -> orderService.create(orderTable.getId() + 1));
        }

        @Test
        void 주문_테이블이_비어있다면_예외가_발생한다() {
            // given
            orderTable.updateEmpty(true);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> orderService.create(orderTable.getId()));
        }

        @Test
        void OrderStatus를_COOKING으로_설정한다() {
            // when
            orderTable.updateEmpty(false);
            orderTableRepository.save(orderTable);
            Long savedOrderId = orderService.create(orderTable.getId());
            Order order = orderRepository.findById(savedOrderId)
                    .orElseThrow(IllegalArgumentException::new);

            // then
            assertThat(order.getOrderStatus()).isEqualTo(COOKING);
        }

        @Test
        void OrderedTime을_설정한다() {
            // when
            orderTable.updateEmpty(false);
            orderTableRepository.save(orderTable);
            Long savedOrderId = orderService.create(orderTable.getId());
            Order order = orderRepository.findById(savedOrderId)
                    .orElseThrow(IllegalArgumentException::new);

            // then
            assertThat(order.getOrderedTime()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        void 저장한_주문_항목들을_반환한다() {
            // when
            orderTable.updateEmpty(false);
            orderTableRepository.save(orderTable);
            Long savedOrderId = orderService.create(orderTable.getId());
            Order order = orderRepository.findById(savedOrderId)
                    .orElseThrow(IllegalArgumentException::new);

            // then
            assertThat(order).isNotNull();
        }

    }

    @Nested
    class 주문_목록을_반환한다 {

        @BeforeEach
        void setUp() {
            OrderTable orderTable = 주문_테이블_생성();
            orderTableRepository.save(orderTable);
            orderRepository.save(주문_생성(orderTable));
        }

        @Test
        void 주문_목록을_반환한다() {
            // when
            List<OrderResponse> orders = orderService.list();

            // then
            assertAll(
                    () -> assertThat(orders).hasSize(1),
                    () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(COOKING)
            );
        }

    }

    @Nested
    class 주문_상태를_변경한다 {

        private Order order;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = 주문_테이블_생성();
            orderTableRepository.save(orderTable);
            order = orderRepository.save(주문_생성(orderTable));
        }

        @Test
        void 주문_상태를_변경한다() {
            // when
            OrderResponse response = orderService.changeOrderStatus(order.getId(), COMPLETION);

            // then
            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(order.getId()),
                    () -> assertThat(response.getOrderStatus()).isEqualTo(COMPLETION)
            );
        }

        @Test
        void 입력받은_orderId가_존재하지_않는다면_예외가_발생한다() {
            // when, then
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.changeOrderStatus(order.getId() + 1L, COMPLETION));
        }

        @Test
        void 저장된_주문이_이미_완료되었다면_예외가_발생한다() {
            // given
            order.updateOrderStatus(COMPLETION);
            orderRepository.save(order);

            // when
            assertThrows(IllegalArgumentException.class,
                    () -> orderService.changeOrderStatus(order.getId(), COMPLETION));
        }

    }

}