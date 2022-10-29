package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.OrderTableCreateRequest;
import kitchenpos.application.dto.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(null, request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.createResponse(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::createResponse)
            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        // Todo: 도메인 로직으로 어떻게 밀어넣을까..?
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderTable.changeSingleEmpty(request.isEmpty());
        return OrderTableResponse.createResponse(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.createResponse(orderTable);
    }
}
