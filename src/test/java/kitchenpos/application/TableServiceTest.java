package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.fixture.domain.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;


    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("OrderTable을 생성한다.")
        void success() {
            OrderTable orderTable = tableService.create(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable());

            OrderTable actual = jdbcTemplateOrderTableDao.findById(orderTable.getId())
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("OrderTable 전체 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = tableService.list();

            assertThat(orderTables).hasSize(8);
        }
    }

    @Nested
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = tableService.create(OrderTableFixture.GUEST_ONE_EMPTY_TRUE.getOrderTable());
        }

        @Test
        @DisplayName("Empty 여부를 변경한다.")
        void success() {
            orderTable.setEmpty(false);
            OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);

            assertThat(actual.isEmpty()).isFalse();
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuests 메서드는")
    class ChangeNumberOfGuests {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = tableService.create(OrderTableFixture.GUEST_ONE_EMPTY_FALSE.getOrderTable());
        }

        @Test
        @DisplayName("손님 수를 변경한다.")
        void success() {
            OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(),
                OrderTableFixture.GUEST_TWO_EMPTY_FALSE.getOrderTable());

            assertThat(actual.getNumberOfGuests()).isEqualTo(2);
        }
    }
}
