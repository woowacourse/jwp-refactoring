package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableChangeEmptyDto;
import kitchenpos.dto.TableChangeNumberOfGuestsDto;
import kitchenpos.dto.TableCreateDto;
import kitchenpos.dto.TableDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public TableDto create(final TableCreateDto request) {
        OrderTable orderTable = request.toDomain();

        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        OrderTable savedTable = orderTableDao.save(orderTable);

        return TableDto.toDto(savedTable);
    }

    public List<TableDto> list() {
        List<OrderTable> orderTables = orderTableDao.findAll();

        return orderTables.stream()
                .map(TableDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(final Long orderTableId, final TableChangeEmptyDto request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(request.isEmpty());
        OrderTable updated = orderTableDao.save(savedOrderTable);

        return TableDto.toDto(updated);
    }

    @Transactional
    public TableDto changeNumberOfGuests(final Long orderTableId, final TableChangeNumberOfGuestsDto request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);
        OrderTable updated = orderTableDao.save(savedOrderTable);

        return TableDto.toDto(updated);
    }
}
