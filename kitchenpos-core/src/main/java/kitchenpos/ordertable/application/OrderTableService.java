package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderStatusValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuests;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.exception.NotExistOrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {
    
    private final OrderTableRepository orderTableRepository;
    
    private final OrderStatusValidator orderStatusValidator;
    
    public OrderTableService(final OrderTableRepository orderTableRepository,
                             final OrderStatusValidator orderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusValidator = orderStatusValidator;
    }
    
    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(
                request.getNumberOfGuests(),
                request.isEmpty());
        return orderTableRepository.save(orderTable);
    }
    
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }
    
    @Transactional
    public OrderTable changeEmpty(final Long orderTableId,
                                  final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블 입니다"));
        savedOrderTable.changeEmpty(orderStatusValidator, request.isEmpty());
        return savedOrderTable;
    }
    
    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuests request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                                               .orElseThrow(() -> new NotExistOrderTable("존재하지 않는 테이블 입니다"));
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return savedOrderTable;
    }
}
