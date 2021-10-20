package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
    }

    @DisplayName("주문 항목의 목록이 비어있으면 안 된다")
    @Test
    void createExceptionEmpty() {
    }

    @DisplayName("주문 항목의 갯수와 메뉴 id의 갯수가 일치하여야 한다")
    @Test
    void createExceptionVerifySize() {
    }

    @DisplayName("주문 테이블이 존재해야 한다")
    @Test
    void createExceptionTableExists() {
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionTableEmpty() {
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
    }

    @DisplayName("주문의 상태를 바꿀 수 있다")
    @Test
    void changeStatus() {
    }

    @DisplayName("기존에 저장되어 있는 주문이 있어야 한다")
    @Test
    void changeStatusExceptionOrderExists() {
    }

    @DisplayName("기존의 주문이 `COMPLETION` 상태이면 안 된다")
    @Test
    void changeStatusExceptionStatus() {
    }
}
