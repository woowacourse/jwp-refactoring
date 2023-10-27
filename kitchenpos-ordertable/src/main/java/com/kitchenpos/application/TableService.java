package com.kitchenpos.application;

import com.kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import com.kitchenpos.application.dto.OrderTableChangeNumberOfGuestRequest;
import com.kitchenpos.application.dto.OrderTableCreateRequest;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.OrderTableRepository;
import com.kitchenpos.event.message.ValidatorHavingMeal;
import com.kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final ApplicationEventPublisher publisher;

    public TableService(final OrderTableRepository orderTableRepository, final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.publisher = publisher;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest req) {
        OrderTable orderTable = req.toDomain();
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest req) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        publisher.publishEvent(new ValidatorHavingMeal(List.of(savedOrderTable.getId())));

        savedOrderTable.updateEmptyStatus(req.isEmpty());
        return savedOrderTable;
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestRequest req) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(req.getNumberOfGuests());

        return savedOrderTable;
    }

    @Transactional(readOnly = true)
    public boolean isExistById(final Long id) {
        return orderTableRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isExistsByIdAndEmptyIsFalse(final Long id) {
        return orderTableRepository.existsByIdAndEmptyIsFalse(id);
    }
}
