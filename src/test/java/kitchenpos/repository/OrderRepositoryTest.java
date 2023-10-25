package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;


    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        TableGroup tableGroupEntity = TableGroup.builder()
                .orderTables(Collections.emptyList())
                .build();
        TableGroup tableGroup = tableGroupRepository.save(tableGroupEntity);

        OrderTable orderTableEntity = OrderTable.builder()
                .tableGroup(tableGroup)
                .numberOfGuests(5)
                .build();
        orderTable = orderTableRepository.save(orderTableEntity);
    }

    @Test
    void 주문_엔티티를_저장한다() {
        Order orderEntity = createOrderEntity();

        Order savedOrder = orderRepository.save(orderEntity);

        assertThat(savedOrder.getId()).isPositive();
    }

    @Test
    void 주문_엔티티를_조회한다() {
        Order orderEntity = createOrderEntity();
        Order savedOrder = orderRepository.save(orderEntity);

        assertThat(orderRepository.findById(savedOrder.getId())).isPresent();
    }

    @Test
    void 모든_주문_엔티티를_조회한다() {
        Order orderEntityA = createOrderEntity();
        Order orderEntityB = createOrderEntity();
        Order savedOrderA = orderRepository.save(orderEntityA);
        Order savedOrderB = orderRepository.save(orderEntityB);

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedOrderA, savedOrderB);
    }

    @Test
    void 주문_테이블에서_주문_상태와_일치하는_엔티티가_존재하면_TRUE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderRepository.save(orderEntity);

        assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(),
                        List.of(orderStatus)
                )
        ).isTrue();
    }

    @Test
    void 주문_테이블에서_주문_상태와_일치하는_엔티티가_존재하지_않으면_FALSE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderRepository.save(orderEntity);

        assertThat(orderRepository.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(),
                        List.of(OrderStatus.COMPLETION)
                )
        ).isFalse();
    }

    @Test
    void 여러_주문_테이블_중_주문_상태와_일치하는_주문_엔티티가_존재하면_TRUE_반환한다() {
        OrderStatus orderStatusA = OrderStatus.MEAL;
        OrderStatus orderStatusB = OrderStatus.COMPLETION;
        Order orderEntity = createOrderEntityWithStatus(orderStatusA);
        orderRepository.save(orderEntity);

        assertThat(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable.getId()),
                        List.of(orderStatusA, orderStatusB)
                )
        ).isTrue();
    }

    @Test
    void 여러_주문_테이블_중_주문_상태와_일치하는_주문_엔티티가_존재하지_않으면_FALSE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderRepository.save(orderEntity);

        assertThat(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable.getId()),
                        List.of(OrderStatus.COOKING)
                )
        ).isFalse();
    }

    private Order createOrderEntity() {
        return Order.builder()
                .orderStatus(OrderStatus.COOKING)
                .orderTable(orderTable)
                .build();
    }

    private Order createOrderEntityWithStatus(OrderStatus orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .orderTable(orderTable)
                .build();
    }
}
