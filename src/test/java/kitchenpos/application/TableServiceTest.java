package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_FALSE;
import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.support.fixture.dto.OrderTableDtoFixture;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.JdbcTemplateOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;


    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("주문 테이블을 생성한다.")
        void success() {
            OrderTable orderTable = GUEST_ONE_EMPTY_TRUE.getOrderTable();
            OrderTableResponse response = tableService.create(OrderTableDtoFixture.상품_생성_요청(orderTable));

            assertThat(response).usingRecursiveComparison()
                .ignoringFields("id", "tableGroupId")
                .isEqualTo(orderTable);
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
            jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
        }

        @Test
        @DisplayName("주문 테이블 전체 목록을 조회한다.")
        void success() {
            List<OrderTableResponse> responses = tableService.list();

            assertThat(responses).hasSize(2);
        }
    }

    @NestedApplicationTest
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable());
        }

        @Test
        @DisplayName("공석 여부를 변경한다.")
        void success() {
            OrderTableResponse response = tableService.changeEmpty(orderTable.getId(), false);

            assertThat(response.isEmpty()).isFalse();
        }
    }

    @NestedApplicationTest
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_FALSE.getOrderTable());
        }

        @Test
        @DisplayName("손님 수를 변경한다.")
        void success() {
            OrderTableResponse response = tableService.changeNumberOfGuests(orderTable.getId(), 2);

            assertThat(response.getNumberOfGuests()).isEqualTo(2);
        }
    }
}
