package kitchenpos.order.domain.repository.converter;

import kitchenpos.order.domain.Order;
import kitchenpos.order.persistence.dto.OrderDataDto;
import kitchenpos.support.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements Converter<Order, OrderDataDto> {

    @Override
    public OrderDataDto entityToData(final Order order) {
        return new OrderDataDto(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime()
        );
    }

    @Override
    public Order dataToEntity(final OrderDataDto orderDataDto) {
        return new Order(orderDataDto.getId(),
                orderDataDto.getOrderTableId(),
                orderDataDto.getOrderStatus(),
                orderDataDto.getOrderedTime()
        );
    }
}
