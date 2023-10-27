package kitchenpos.domain.order;  
  
import kitchenpos.configuration.Validator;  
import kitchenpos.domain.table.OrderTable;  
import kitchenpos.repository.OrderTableRepository;  
  
@Validator  
public class OrderValidator {  
  
    private final OrderTableRepository orderTableRepository;  
  
    public OrderValidator(final OrderTableRepository orderTableRepository) {  
        this.orderTableRepository = orderTableRepository;  
    }  
  
    public void validate(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                                                          .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));  
  
        if (orderTable.isEmpty()) {  
            throw new IllegalArgumentException("빈 테이블에는 주문을 생성할 수 없습니다.");  
        }  
    }  
}
