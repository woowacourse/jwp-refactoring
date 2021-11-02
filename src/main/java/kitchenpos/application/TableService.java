package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyChangeRequest;
import kitchenpos.dto.request.TableGuestChangeRequest;
import kitchenpos.dto.response.TableResponse;
import kitchenpos.exception.KitchenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable saved = orderTableRepository.save(orderTable);
        return TableResponse.of(saved);
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(TableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId,
        final TableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new KitchenException("존재하지 않는 테이블입니다."));
        savedOrderTable.changeEmpty(request.isEmpty());
        return TableResponse.of(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId,
        final TableGuestChangeRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new KitchenException("존재하지 않는 테이블입니다."));
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.of(savedOrderTable);
    }
}
