package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateTableDto;
import kitchenpos.application.dto.EmptyTableDto;
import kitchenpos.application.dto.TableDto;
import kitchenpos.application.dto.UpdateGuestNumberDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    @Transactional
    public TableDto create(final CreateTableDto createTableDto) {
        OrderTable orderTable = new OrderTable(createTableDto.getNumberOfGuests(), createTableDto.getEmpty());
        return TableDto.of(orderTableDao.save(orderTable));
    }

    public List<TableDto> list() {
        return orderTableDao.findAll()
                .stream()
                .map(TableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableDto changeEmpty(EmptyTableDto emptyTableDto) {
        Long orderTableId = emptyTableDto.getOrderTableId();
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(emptyTableDto.getEmpty());

        return TableDto.of(orderTableDao.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(UpdateGuestNumberDto updateGuestNumberDto) {
        final Long orderTableId = updateGuestNumberDto.getOrderTableId();
        final int numberOfGuests = updateGuestNumberDto.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return TableDto.of(orderTableDao.save(savedOrderTable));
    }
}
