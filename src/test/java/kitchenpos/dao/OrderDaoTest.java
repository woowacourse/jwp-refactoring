package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;


    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        TableGroup tableGroupEntity = new TableGroup();
        tableGroupEntity.setCreatedDate(LocalDateTime.now());
        TableGroup tableGroup = tableGroupDao.save(tableGroupEntity);

        OrderTable orderTableEntity = new OrderTable();
        orderTableEntity.setTableGroupId(tableGroup.getId());
        orderTable = orderTableDao.save(orderTableEntity);
    }

    @Test
    void 주문_엔티티를_저장한다() {
        Order orderEntity = createOrderEntity();

        Order saveOrder = orderDao.save(orderEntity);

        assertThat(saveOrder.getId()).isPositive();
    }

    @Test
    void 주문_엔티티를_조회한다() {
        Order orderEntity = createOrderEntity();
        Order saveOrder = orderDao.save(orderEntity);

        assertThat(orderDao.findById(saveOrder.getId())).isPresent();
    }

    @Test
    void 모든_주문_엔티티를_조회한다() {
        Order orderEntityA = createOrderEntity();
        Order orderEntityB = createOrderEntity();
        Order saveOrderA = orderDao.save(orderEntityA);
        Order saveOrderB = orderDao.save(orderEntityB);

        List<Order> orders = orderDao.findAll();

        assertThat(orders).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveOrderA, saveOrderB);
    }

    @Test
    void 주문_테이블에서_주문_상태와_일치하는_엔티티가_존재하면_TRUE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderDao.save(orderEntity);

        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(),
                        List.of(orderStatus.name())
                )
        ).isTrue();
    }

    @Test
    void 주문_테이블에서_주문_상태와_일치하는_엔티티가_존재하지_않으면_FALSE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderDao.save(orderEntity);

        assertThat(orderDao.existsByOrderTableIdAndOrderStatusIn(
                        orderTable.getId(),
                        List.of(OrderStatus.COMPLETION.name())
                )
        ).isFalse();
    }

    @Test
    void 여러_주문_테이블_중_주문_상태와_일치하는_주문_엔티티가_존재하면_TRUE_반환한다() {
        OrderStatus orderStatusA = OrderStatus.MEAL;
        OrderStatus orderStatusB = OrderStatus.COMPLETION;
        Order orderEntity = createOrderEntityWithStatus(orderStatusA);
        orderDao.save(orderEntity);

        assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable.getId()),
                        List.of(orderStatusA.name(), orderStatusB.name())
                )
        ).isTrue();
    }

    @Test
    void 여러_주문_테이블_중_주문_상태와_일치하는_주문_엔티티가_존재하지_않으면_FALSE_반환한다() {
        OrderStatus orderStatus = OrderStatus.MEAL;
        Order orderEntity = createOrderEntityWithStatus(orderStatus);
        orderDao.save(orderEntity);

        assertThat(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        List.of(orderTable.getId()),
                        List.of(OrderStatus.COOKING.name())
                )
        ).isFalse();
    }

    private Order createOrderEntity() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(null);
        return order;
    }

    private Order createOrderEntityWithStatus(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(null);
        return order;
    }
}
