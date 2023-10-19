package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.NumberOfGuests;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest numberOfGuestsAndEmptyStatus) {
        final OrderTable orderTable = new OrderTable(
                new NumberOfGuests(numberOfGuestsAndEmptyStatus.getNumberOfGuests()),
                numberOfGuestsAndEmptyStatus.isEmpty()
        );
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest emptyInput) {
        final OrderTable orderTable = orderTableRepository.findMandatoryById(orderTableId);
        validateTableGroupNotNull(orderTable);
        validateOrderStatusCompletion(orderTable);
        orderTable.changeEmpty(emptyInput.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateTableGroupNotNull(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatusCompletion(final OrderTable orderTable) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final OrderTableChangeGuestNumberRequest numberOfGuestsInput
    ) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(numberOfGuestsInput.getNumberOfGuests());
        final OrderTable orderTable = orderTableRepository.findMandatoryById(orderTableId);
        validateOrderTableOccupied(orderTable);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateOrderTableOccupied(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
