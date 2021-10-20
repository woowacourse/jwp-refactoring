package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
    }

    @DisplayName("주문 테이블을 비울 수 있다")
    @Test
    void clear() {
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void clearExceptionExists() {
    }

    @DisplayName("주문 테이블의 단체 지정이 되어 있지 않아야 한다")
    @Test
    void clearExceptionNonGroup() {
    }

    @DisplayName("주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL` 이면 예외가 발생한다")
    @Test
    void clearExceptionExistsAndStatus() {
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 수정할 수 있다")
    @Test
    void changeNumberOfGuests() {
    }

    @DisplayName("방문한 손님 수는 0 명 이상이어야 한다")
    @Test
    void changeNumberOfGuestsExceptionUnderZero() {
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsExceptionExists() {
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어있으면 안 된다")
    @Test
    void changeNumberOfGuestsExceptionEmpty() {
    }
}
