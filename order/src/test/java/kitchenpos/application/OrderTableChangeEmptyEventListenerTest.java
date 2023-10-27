package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.exception.UnCompletedOrderExistsException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menu.ProductIdAndQuantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Order.MenuIdQuantityAndPrice;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableChangeEmptyEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderTableChangeEmptyEventListenerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderTableChangeEmptyEventListener listener;

    @Test
    void 변경하려는_테이블의_주문_상태가_변경_불가하면_예외를_반환한다() {
        // given
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Product product = Product.of("product", new BigDecimal(2500));
        em.persist(menuGroup);
        em.persist(product);
        OrderTable orderTable = new OrderTable(4, false);
        em.persist(orderTable);
        Menu menu = Menu.of("menu", new BigDecimal(10_000), menuGroup.getId(),
                List.of(new ProductIdAndQuantity(product.getId(), 4L)));
        em.persist(menu);
        Order order = Order.of(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                List.of(new MenuIdQuantityAndPrice(menu.getId(), 1L, menu.getPrice())));
        em.persist(order);
        em.flush();
        em.clear();

        OrderTableChangeEmptyEvent event = new OrderTableChangeEmptyEvent(orderTable.getId(), true);

        // when, then
        assertThrows(UnCompletedOrderExistsException.class,
                () -> listener.handleOrderTableChangeEmptyEvent(event));
    }
}
