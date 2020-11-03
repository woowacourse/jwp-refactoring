package kitchenpos.table.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.exception.IllegalOrderStatusException;
import kitchenpos.table.domain.Table;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableEmptyEditRequest;
import kitchenpos.table.dto.TableGuestEditRequest;
import kitchenpos.table.dto.TableResponses;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Long create(TableCreateRequest request) {
        Table table = request.toEntity();
        return orderTableRepository.save(table).getId();
    }

    public TableResponses list() {
        List<Table> tables = orderTableRepository.findAll();
        return TableResponses.from(tables);
    }

    @Transactional
    public void editEmpty(final Long orderTableId, TableEmptyEditRequest request) {
        final Table savedTable = findOne(orderTableId);

        if (orderRepository.existsByTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalOrderStatusException("테이블을 비울 때, 유효하지않은 OrderStatus 상태입니다.");
        }

        savedTable.changeEmpty(request.getEmpty());
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final TableGuestEditRequest request) {
        Table savedTable = findOne(orderTableId);
        savedTable.changeNumberOfGuests(request.getNumberOfGuests());
    }

    private Table findOne(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
