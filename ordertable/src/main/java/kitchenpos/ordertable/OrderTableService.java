package kitchenpos.ordertable;

import kitchenpos.ordertable.ui.OrderTableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderTableService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public OrderTableService(final OrderTableValidator orderTableValidator,
                             final OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableDto orderTableDto) {
        final OrderTable orderTable = new OrderTable(orderTableDto.getNumberOfGuests(), orderTableDto.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다. 빈 테이블로 변경할 수 없습니다."));

        orderTableValidator.validateChangeEmptyTableOrderCondition(orderTable.getId());
        orderTable.changeEmpty();

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다. 방문한 손님 수를 등록할 수 없습니다."));

        orderTable.changeNumberOfGuest(orderTableDto.getNumberOfGuests());

        return orderTableRepository.save(orderTable);
    }
}
