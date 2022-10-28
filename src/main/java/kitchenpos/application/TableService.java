package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyUpdateRequest;
import kitchenpos.dto.request.TableGuestUpdateRequest;
import kitchenpos.dto.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
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

        savedOrderTable.validateNotInTableGroup();
        validateIsCompletedOrder(orderTableId);
        OrderTable updatedOrderTable = orderTableRepository.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }

    private void validateIsCompletedOrder(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public TableResponse changeNumberOfGuests(Long orderTableId, TableGuestUpdateRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId);
        savedOrderTable.validateNotEmpty();
        OrderTable updatedOrderTable = orderTableRepository.save(request.toUpdateEntity(savedOrderTable));

        return TableResponse.from(updatedOrderTable);
    }
}
