package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static kitchenpos.fixture.FixtureFactory.createOrderTable;
import static kitchenpos.ui.TableRestController.TABLE_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest extends ControllerTest {
    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블 생성 요청")
    @Test
    void create() throws Exception {
        OrderTable request = createOrderTable(null, null, 0, true);
        String body = objectMapper.writeValueAsString(request);

        when(tableService.create(any())).thenReturn(new OrderTable());

        requestWithPost(TABLE_API, body);
    }

    @DisplayName("주문 테이블 목록 조회 요청")
    @Test
    void list() throws Exception {
        requestWithGet(TABLE_API);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경 요청")
    @Test
    void changeEmpty() throws Exception {
        OrderTable request = createOrderTable(null, null, 0, false);
        String body = objectMapper.writeValueAsString(request);

        when(tableService.changeEmpty(anyLong(), any())).thenReturn(new OrderTable());

        requestWithPut(TABLE_API + "/1/empty", body);
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경 요청")
    @Test
    void changeNumberOfGuests() throws Exception {
        OrderTable request = createOrderTable(null, null, 4, false);
        String body = objectMapper.writeValueAsString(request);

        when(tableService.changeNumberOfGuests(anyLong(), any())).thenReturn(new OrderTable());

        requestWithPut(TABLE_API + "/1/number-of-guests", body);
    }
}