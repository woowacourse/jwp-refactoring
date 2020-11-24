package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.dto.table.TableChangeRequest;
import kitchenpos.dto.table.TableCreateRequest;
import kitchenpos.dto.table.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderDao orderDao;
    private final TableDao tableDao;

    public TableService(final OrderDao orderDao, final TableDao tableDao) {
        this.orderDao = orderDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest orderTable) {
        Table table = tableDao.save(new Table(orderTable.getNumberOfGuests(), orderTable.isEmpty()));

        return TableResponse.of(table);
    }

    public List<TableResponse> list() {
        return tableDao.findAll()
            .stream()
            .map(TableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableChangeRequest request) {
        final boolean isEmptyWantedResult = request.isEmpty();
        final Table targetTable = tableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (isEmptyWantedResult && isNotMealOver(orderTableId)) {
            throw new IllegalArgumentException("식사가 아직 끝나지 않은 테이블을 비울 수 없습니다.");
        }
        targetTable.changeEmpty(isEmptyWantedResult);

        return TableResponse.of(tableDao.save(targetTable));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableChangeRequest request) {
        final Table savedTable = tableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        int numberOfGuests = request.getNumberOfGuests();

        if (savedTable.isEmpty() && numberOfGuests != 0) {
            throw new IllegalArgumentException("비어있는 테이블의 손님 수를 0이 아닌 수로 바꿀 수 없습니다.");
        }
        savedTable.changeNumberOfGuests(numberOfGuests);

        return TableResponse.of(tableDao.save(savedTable));
    }

    private boolean isNotMealOver(Long orderTableId) {
        return orderDao.existsByTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
