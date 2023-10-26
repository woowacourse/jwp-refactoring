package kitchenpos.util;

import kitchenpos.listener.CreatedMenuProductEventListener;
import kitchenpos.listener.OrderStatusEventListener;
import kitchenpos.listener.OrderTableEventListener;
import kitchenpos.listener.RequestEventListener;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public OrderStatusEventListener orderStatusEventListener(final OrderRepository orderRepository) {
        return new OrderStatusEventListener(orderRepository);
    }

    @Bean
    public OrderTableEventListener orderTableEventListener(final OrderTableRepository orderTableRepository) {
        return new OrderTableEventListener(orderTableRepository);
    }

    @Bean
    public RequestEventListener requestEventListenerr() {
        return new RequestEventListener();
    }

    @Bean
    public CreatedMenuProductEventListener createdMenuProductEventListener(final ProductRepository productRepository) {
        return new CreatedMenuProductEventListener(productRepository);
    }
}
