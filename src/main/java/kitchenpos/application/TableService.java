package kitchenpos.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyRequest;
import kitchenpos.dto.TableChangeGuestsRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableResponse;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest tableCreateRequest) {
        final OrderTable orderTable = tableCreateRequest.toEntity();

        return TableResponse.of(orderTableDao.save(orderTable));
    }

    public List<TableResponse> list() {
        return TableResponse.ofList(orderTableDao.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyRequest tableChangeEmptyRequest) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을수가 없습니다."));

        savedOrderTable.existTableGroupId();

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("요리중이거나 식사중이면 상태를 변경할 수 없습니다.");
        }

        savedOrderTable.changeEmpty(tableChangeEmptyRequest.isEmpty());

        final OrderTable orderTable = orderTableDao.save(savedOrderTable);

        return TableResponse.of(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId,
            final TableChangeGuestsRequest tableChangeGuestsRequest) {
        final int numberOfGuests = tableChangeGuestsRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        final OrderTable orderTable = orderTableDao.save(savedOrderTable);

        return TableResponse.of(orderTable);
    }
}
