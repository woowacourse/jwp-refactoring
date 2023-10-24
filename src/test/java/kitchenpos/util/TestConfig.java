package kitchenpos.util;

import kitchenpos.listener.OrderStatusEventListener;
import kitchenpos.listener.OrderTableEventListener;
import kitchenpos.listener.RequestEventListener;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public OrderStatusEventListener orderStatusEventListener(final OrderRepository orderRepository){
        return new OrderStatusEventListener(orderRepository);
    }

    @Bean
    public OrderTableEventListener orderTableEventListener(final OrderTableRepository orderTableRepository){
        return new OrderTableEventListener(orderTableRepository);
    }

    @Bean
    public RequestEventListener eventListener(){
        return new RequestEventListener();
    }
}
