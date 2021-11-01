package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderTableEmptyRequest;
import kitchenpos.dto.request.OrderTableNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listFrom(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyRequest orderTableEmptyRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);

        //TODO: Order 리팩터링시 수정하기
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(orderTableEmptyRequest.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableNumberOfGuestsRequest.getNumberOfGuests());

        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("존재 하지 않는 테이블입니다.(id: %d)", orderTableId)
            ));
    }
}
