package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setId(null);
        orderTable.setTableGroupId(null);
        // TODO numberOfGuests 가 음수여선 안된다는 유효성 검증 추가 필요

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        // 아이디로 테이블을 찾는다. 없으면 예외를 던진다.
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        // 찾은 테이블에 그룹 아이디가 없어야 한다. 있으면 예외를 던진다.
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        // 찾은 테이블에 COOKING, MEAL 상태인 주문이 없어야 한다. 있으면 예외를 던진다.
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        // 주문테이블을 비운다. body로 받은 값을 여기서 한 번 쓴다. true/false이다.
        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        // RequestBody로 받은 값을 사용하는 유일한 곳이다. 변경할 테이블 고객 수만 받는다.
        final int numberOfGuests = orderTable.getNumberOfGuests();

        // 음수로 변경할 순 없다. 0은 가능하다.
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        // PathVariable로 받은 테이블이 존재하지 않으면 예외를 던진다
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        // 찾은 테이블이 주문 불가 상태 테이블일 경우 고객 수 변경은 잘못된 요청이다. 예외를 던진다
        // IllegalStateException이 어울릴 것 같은데?
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        // 고객수를 변경하고 업데이트한다
        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
