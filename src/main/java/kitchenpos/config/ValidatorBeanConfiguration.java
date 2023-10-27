package kitchenpos.config;

import kitchenpos.menu.domain.MenuExistenceValidatorImpl;
import kitchenpos.menu.domain.MenuGroupValidator;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroupValidatorImpl;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.order.domain.MenuExistenceValidator;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.domain.OrderValidatorImpl;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableValidatorImpl;
import kitchenpos.ordertable.domain.OrdersInTableCompleteValidator;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.OrdersInTablesCompleteValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorBeanConfiguration {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public ValidatorBeanConfiguration(final OrderRepository orderRepository,
                                      final OrderTableRepository orderTableRepository,
                                      final MenuRepository menuRepository,
                                      final MenuGroupRepository menuGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Bean
    public OrderTableValidator orderTableValidator() {
        return new OrderTableValidatorImpl(orderTableRepository);
    }

    @Bean
    public MenuExistenceValidator menuExistenceValidator() {
        return new MenuExistenceValidatorImpl(menuRepository);
    }

    @Bean
    public MenuGroupValidator menuGroupValidator() {
        return new MenuGroupValidatorImpl(menuGroupRepository);
    }

    @Bean
    public OrderValidatorImpl orderValidator() {
        return new OrderValidatorImpl(orderRepository);
    }

    @Bean
    public OrdersInTableCompleteValidator ordersInTableCompleteValidator() {
        return orderValidator();
    }

    @Bean
    public OrdersInTablesCompleteValidator ordersInTablesCompleteValidator() {
        return orderValidator();
    }
}
