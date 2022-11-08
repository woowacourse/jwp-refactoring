package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableValidator;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyUpdateRequest;
import kitchenpos.dto.request.TableGuestUpdateRequest;
import kitchenpos.dto.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableResponse create(TableCreateRequest request) {
        OrderTable savedOrderTable = orderTableRepository.save(request.toEntity());
        return TableResponse.from(savedOrderTable);
    }

    public List<TableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return toTableResponses(orderTables);
    }

    private List<TableResponse> toTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId);
        tableValidator.ableToChangeEmpty(savedOrderTable);
        OrderTable updatedOrderTable = orderTableRepository.update(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, TableGuestUpdateRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId);
        tableValidator.ableToChangeNumberOfGuests(savedOrderTable);
        OrderTable updatedOrderTable = orderTableRepository.update(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }
}
