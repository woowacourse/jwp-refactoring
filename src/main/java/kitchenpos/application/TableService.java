package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.GuestNumber;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableDto;
import kitchenpos.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final TableRepository tableRepository;

    public TableService(OrderDao orderDao, TableRepository tableRepository) {
        this.orderDao = orderDao;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableDto create(TableDto tableDto) {
        OrderTable orderTable =
                new OrderTable(null, GuestNumber.from(tableDto.getNumberOfGuests()), tableDto.isEmpty(), null);
        OrderTable savedOrderTable = tableRepository.save(orderTable);
        return new TableDto(savedOrderTable);
    }

    public List<TableDto> list() {
        return tableRepository.findAll()
                .stream()
                .map(TableDto::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableDto changeEmpty(Long orderTableId, TableDto table) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.");
        }

        savedOrderTable.setEmpty(table.isEmpty());

        return new TableDto(tableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableDto changeNumberOfGuests(Long orderTableId, TableDto table) {
        OrderTable savedOrderTable = tableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeGuestNumber(GuestNumber.from(table.getNumberOfGuests()));
        return new TableDto(tableRepository.save(savedOrderTable));
    }
}
