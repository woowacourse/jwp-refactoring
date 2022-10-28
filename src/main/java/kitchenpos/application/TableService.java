package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_CHANGE_NUMBER_OF_GUEST;
import static kitchenpos.application.exception.ExceptionType.INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTable create(final OrderTable orderTable) {
        // todo 왜 setId를 해주는지?
        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));

        // todo 진행중인 테이블이 존재하는지 검증하는 로직이니까 OrderTable 로 이동
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new CustomIllegalArgumentException(INVALID_PROCEEDING_TABLE_GROUP_EXCEPTION);
        }

        // todo 요리중이거나, 식사중인 주문이 있는지 검증하는 로직이라서  Order -> OrderStatus 로 가는게 맞지 않을까
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        // :todo OrderTable 내에 인원 검증 로직이니까 이동
        if (numberOfGuests < 0) {
            throw new CustomIllegalArgumentException(INVALID_CHANGE_NUMBER_OF_GUEST);
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));

        // :todo OrderTable 없는 테이블에 대한 요청인지 검증
        if (savedOrderTable.isEmpty()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
