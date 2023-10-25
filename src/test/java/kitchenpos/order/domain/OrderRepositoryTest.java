package kitchenpos.order.domain;

import kitchenpos.menu.domain.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.RepositoryTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class OrderRepositoryTest extends RepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문을_조회할_때_주문_아이템과_함께_조회횐다() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(new MenuGroupName("one plus one")));
        Product product = productRepository.save(new Product(new ProductName("pizza"), new ProductPrice(BigDecimal.valueOf(14000L))));
        Menu menu = new Menu(new MenuName("pizza one plus one"), new MenuPrice(BigDecimal.valueOf(27000L)), menuGroup);
        MenuProduct menuProduct = new MenuProduct(product, new MenuProductQuantity(2L));
        menu.addMenuProducts(List.of(menuProduct));
        menuRepository.save(menu);

        OrderTable orderTable = new OrderTable(new GuestNumber(10), false);
        orderTableRepository.save(orderTable);

        Order order = new Order(LocalDateTime.now());
        OrderLineItem orderLineItem = new OrderLineItem(menu, new OrderLineItemQuantity(1L));
        OrderLineItem orderLineItem_2 = new OrderLineItem(menu, new OrderLineItemQuantity(2L));
        order.addOrderLineItems(List.of(orderLineItem, orderLineItem_2));
        orderTable.addOrder(order);
        orderRepository.save(order);
        em.flush();
        em.clear();

        // when
        List<Order> orders = orderRepository.findAllWithOrderLineItems();
        for (Order findOrder : orders) {
            em.detach(findOrder);
        }

        // then
        for (Order findOrder : orders) {
            Assertions.assertThatNoException()
                    .isThrownBy(() -> findOrder.getOrderLineItems().get(0).getSeq());
        }
    }
}
