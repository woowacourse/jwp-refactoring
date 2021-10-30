package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
                                                     .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        table.changeEmpty(orderTable.isEmpty());
        return table;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
                                                     .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        table.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return table;
    }
}
