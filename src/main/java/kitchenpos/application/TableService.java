package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableCreationRequest;
import kitchenpos.dto.TableEmptyUpdateRequest;
import kitchenpos.dto.TableNumberOfGuestsUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(TableCreationRequest request) {
        OrderTable orderTable = OrderTable.create(request.getNumberOfGuests(), request.getEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeEmpty(request.getEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, TableNumberOfGuestsUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTable;
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 주문 테이블이 존재하지 않습니다."));
    }
}
