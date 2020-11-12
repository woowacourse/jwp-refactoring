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
        Table table = tableDao.save(orderTable.toEntity());

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
        final Table savedTable = tableDao.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (savedTable.isGrouped() && request.isEmpty()) {
            throw new IllegalArgumentException("그룹에 속한 테이블을 빈 테이블로 바꿀 수 없습니다."
                + "해당 테이블은 그룹이 해제되면 자동으로 비워집니다.");
        }
        if (isNotMealOver(orderTableId)) {
            throw new IllegalArgumentException();
        }
        savedTable.changeEmpty(request.isEmpty());

        return TableResponse.of(tableDao.save(savedTable));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableChangeRequest request) {
        final Table savedTable = tableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        if (savedTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블의 손님 수를 바꿀 수 없습니다.");
        }
        savedTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableResponse.of(tableDao.save(savedTable));
    }

    private boolean isNotMealOver(Long orderTableId) {
        return orderDao.existsByTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }
}
