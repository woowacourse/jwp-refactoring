package kitchenpos.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class JdbcTemplateOrderLineItemRepositoryTest {

    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;

    private Long orderId;

    @Autowired
    public JdbcTemplateOrderLineItemRepositoryTest(final OrderLineItemRepository orderLineItemRepository,
                                                   final OrderRepository orderRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setup() {
        this.orderId = orderRepository.save(new Order(
                null,
                1L,
                COOKING,
                LocalDateTime.now(),
                new ArrayList<>()
        )).getId();
    }

    @Test
    void 저장한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1L);

        // when
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrderLineItem.getSeq()).isNotNull(),
                () -> assertThat(savedOrderLineItem.getQuantity()).isEqualTo(1L),
                () -> assertThat(savedOrderLineItem.getMenuId()).isEqualTo(1L),
                () -> assertThat(savedOrderLineItem.getOrderId()).isEqualTo(orderId)
        );
    }

    @Test
    void ID로_조회한다() {
        //before
        orderRepository.save(new Order(null, 1L, COOKING, LocalDateTime.now(), new ArrayList<>()));

        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1L);
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemRepository.findById(savedOrderLineItem.getSeq());

        // then
        Assertions.assertAll(
                () -> assertThat(foundOrderLineItem).isPresent(),
                () -> assertThat(foundOrderLineItem.get())
                        .usingRecursiveComparison()
                        .ignoringFields("seq")
                        .isEqualTo(orderLineItem)
        );
    }

    @Test
    void 일치하는_ID가_없을_경우_empty를_반환한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1L);
        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        Long notExistId = -1L;

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemRepository.findById(notExistId);

        // then
        assertThat(foundOrderLineItem).isEmpty();
    }

    @Test
    void 목록을_조회한다() {
        // given
        OrderLineItem orderLineItem1 = new OrderLineItem(orderId, 1L, 1L);
        orderLineItemRepository.save(orderLineItem1);

        OrderLineItem orderLineItem2 = new OrderLineItem(orderId, 2L, 3L);
        orderLineItemRepository.save(orderLineItem2);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();

        // then
        assertThat(orderLineItems).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(
                                orderLineItem1, orderLineItem2
                        )
                );
    }

    @Test
    void order_id로_조회한다() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem(orderId, 1L, 1L);
        orderLineItemRepository.save(orderLineItem);

        // when
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        // then
        assertThat(orderLineItems).hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(
                        Arrays.asList(orderLineItem)
                );
    }
}
