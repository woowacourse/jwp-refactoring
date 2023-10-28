package listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventListener {
//
//    private final OrderTableRepository orderTableRepository;
//
//    public OrderTableEventListener(final OrderTableRepository orderTableRepository) {
//        this.orderTableRepository = orderTableRepository;
//    }
//
//    @EventListener
//    public void validateOrderTable(final OrderTableIdValidateEvent event) {
//        final OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
//                .orElseThrow(() -> new NoSuchDataException("입력한 id의 테이블이 존재하지 않습니다."));
//
//        if (orderTable.isEmpty()) {
//            throw new EmptyTableException("비어있는 테이블의 주문은 생성할 수 없습니다.");
//        }
//    }
}
