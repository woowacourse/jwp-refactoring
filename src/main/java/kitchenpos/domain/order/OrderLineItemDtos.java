package kitchenpos.domain.order;

import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.exception.InvalidOrderLineItemDtosException;
import kitchenpos.util.ValidateUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderLineItemDtos {
    private final List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos;

    private OrderLineItemDtos(List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos) {
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderLineItemDtos from(List<OrderCreateRequest.OrderLineItemDto> orderLineItemDtos) {
        ValidateUtil.validateNonNull(orderLineItemDtos);
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new InvalidOrderLineItemDtosException("주문 항목이 없습니다!");
        }

        return new OrderLineItemDtos(orderLineItemDtos);
    }

    public List<Long> getMenuIds() {
        return this.orderLineItemDtos.stream()
                .map(OrderCreateRequest.OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());
    }

    public boolean isNotEqualSize(long size) {
        return this.orderLineItemDtos.size() != size;
    }

    public Stream<OrderCreateRequest.OrderLineItemDto> stream() {
        return orderLineItemDtos.stream();
    }
}
