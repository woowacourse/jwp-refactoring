package kitchenpos.domain.order;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collections;

import static kitchenpos.MenuFixture.createMenu1;
import static kitchenpos.MenuFixture.createMenuGroup1;
import static kitchenpos.OrderFixture.createOrder;
import static kitchenpos.OrderFixture.createOrderLineItem;
import static kitchenpos.ProductFixture.createProduct1;
import static kitchenpos.TableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("주어진 주문 테이블과 주문 상태들을 가진 주문이 있는지 확인한다.")
    @Test
    void existsByOrderTableAndOrderStatusIn() {
        final MenuGroup menuGroup1 = createMenuGroup1();
        final Product product1 = createProduct1();
        final Menu menu = createMenu1(menuGroup1, Collections.singletonList(product1));
        final OrderTable orderTable1 = createOrderTable();
        final OrderTable orderTable2 = createOrderTable();
        final OrderLineItem orderLineItem = createOrderLineItem(menu);
        final Order order = createOrder(orderTable1, Collections.singletonList(orderLineItem));

        em.persist(menuGroup1);
        em.persist(product1);
        em.persist(menu);
        em.persist(orderTable1);
        em.persist(orderTable2);
        em.persist(order);
        em.flush();

        final boolean isExist = orderRepository.existsByOrderTableAndOrderStatusIn(orderTable1, Collections.singletonList(order.getOrderStatus()));
        final boolean isNotExist = orderRepository.existsByOrderTableAndOrderStatusIn(orderTable2, Collections.singletonList(OrderStatus.COOKING));

        assertThat(isExist).isTrue();
        assertThat(isNotExist).isFalse();
    }
}