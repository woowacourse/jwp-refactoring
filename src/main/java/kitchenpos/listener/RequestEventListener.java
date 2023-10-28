package kitchenpos.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RequestEventListener {
//
//    @EventListener
//    public void validateCreateTableGroupRequest(final CreateTableGroupRequest request) {
//        final List<OrderTableRequest> orderTables = request.getOrderTables();
//
//        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
//            throw new InvalidOrderStateException("테이블 2개 이상부터 그룹을 형성할 수 있습니다.");
//        }
//    }
//
//    @EventListener
//    public void validateCreateOrderRequest(final CreateOrderRequest request) {
//        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
//            throw new EmptyListException("아이템이 비어있습니다.");
//        }
//    }
}
