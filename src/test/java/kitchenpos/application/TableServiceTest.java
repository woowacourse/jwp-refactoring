package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.support.fixture.domain.OrderTableFixture;
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
        @DisplayName("OrderTable을 생성한다.")
        void success() {
            OrderTable orderTable = GUEST_ONE_EMPTY_TRUE.getOrderTable();
            OrderTable actual = tableService.create(orderTable);

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
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
        @DisplayName("OrderTable 전체 목록을 조회한다.")
        void success() {
            List<OrderTable> orderTables = tableService.list();

            assertThat(orderTables).hasSize(2);
        }
    }

    @NestedApplicationTest
    @DisplayName("changeEmpty 메서드는")
    class ChangeEmpty {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            orderTable = tableService.create(GUEST_ONE_EMPTY_TRUE.getOrderTable());
        }

        @Test
        @DisplayName("Empty 여부를 변경한다.")
        void success() {
            orderTable.setEmpty(false);
            OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);

            assertThat(actual.isEmpty()).isFalse();
        }
    }

    @NestedApplicationTest
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
