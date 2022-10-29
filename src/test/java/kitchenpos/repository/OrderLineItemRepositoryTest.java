package kitchenpos.repository;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderLineItemRepositoryTest {

    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;

    private Long orderId;

    @Autowired
    public OrderLineItemRepositoryTest(final OrderLineItemRepository orderLineItemRepository,
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
                Arrays.asList(new OrderLineItem(1L, 3L))
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
                () -> assertThat(savedOrderLineItem.getMenuId()).isEqualTo(1L)
        );
    }

    @Test
    void ID로_조회한다() {
        //before
        orderRepository.save(
                new Order(null, 1L, COOKING, LocalDateTime.now(), Arrays.asList(new OrderLineItem(1L, 3L))));

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
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();
        assertThat(orderLineItems).hasSize(1)
                .extracting("menuId", "quantity")
                .contains(tuple(1L, 3L));
    }
}
