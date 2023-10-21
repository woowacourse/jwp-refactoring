package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyStatusUpdateRequest;
import kitchenpos.dto.request.TableNumberOfGuestsUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final TableCreateRequest request) {
        final OrderTable orderTable = OrderTable.create(request.getNumberOfGuests(), request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableEmptyStatusUpdateRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중 또는 식사중인 주문 테이블은 빈 테이블로 변경할 수 없습니다.");
        }

        orderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableNumberOfGuestsUpdateRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableDao.save(orderTable);
    }
}
