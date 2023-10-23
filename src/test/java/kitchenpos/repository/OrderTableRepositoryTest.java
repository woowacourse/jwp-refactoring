package kitchenpos.repository;

import kitchenpos.BaseTest;
import kitchenpos.domain.order.GuestNumber;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class OrderTableRepositoryTest extends BaseTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 주문_테이블을_조회할_때_주문들도_함께_조회한다() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable firstOrderTable = new OrderTable(new GuestNumber(1), true);
        OrderTable secondOrderTable = new OrderTable(new GuestNumber(2), true);
        tableGroup.addOrderTables(List.of(firstOrderTable, secondOrderTable));
        orderTableRepository.save(firstOrderTable);
        orderTableRepository.save(secondOrderTable);
        tableGroupRepository.save(tableGroup);

        Order firstOrder = new Order(LocalDateTime.now());
        Order secondOrder = new Order(LocalDateTime.now());

        firstOrderTable.addOrder(firstOrder);
        secondOrderTable.addOrder(secondOrder);
        orderRepository.save(firstOrder);
        orderRepository.save(secondOrder);

        em.flush();
        em.clear();

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupIdWithOrders(tableGroup.getId());
        for (OrderTable orderTable : orderTables) {
            em.detach(orderTable);
        }

        // then
        for (OrderTable orderTable : orderTables) {
            Assertions.assertThatNoException()
                    .isThrownBy(() -> orderTable.getOrders().forEach(Order::getOrderStatus));
        }
    }
}