package kitchenpos.application;

import kitchenpos.application.test.ServiceUnitTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.fixture.OrderTableFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static kitchenpos.domain.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TableServiceTest extends ServiceUnitTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        OrderTable 주문_테이블 = 주문_테이블_생성();
        this.orderTable = 주문_테이블;
    }

    @Nested
    class 주문_테이블을_저장한다 {

        @Test
        void 주문_테이블을_저장한다() {
            // when, then
            assertDoesNotThrow(
                    () -> tableService.create(orderTable)
            );
        }

    }

}