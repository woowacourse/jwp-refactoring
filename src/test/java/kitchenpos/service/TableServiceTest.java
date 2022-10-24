package kitchenpos.service;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.OrderTableUpdateGuestsRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void create() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(3L, false);

        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        assertAll(
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(orderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void create_numberOfGuestsNull() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(null, false);

        assertThatThrownBy(() -> tableService.create(orderTableCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_numberOfGuestsNegative() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(-1L, false);

        assertThatThrownBy(() -> tableService.create(orderTableCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(5);
    }

    @Test
    void changeEmpty() {
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(false);

        OrderTable savedOrderTable = tableService.changeEmpty(1L, orderTableUpdateEmptyRequest);

        assertAll(
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(savedOrderTable.isEmpty()).isFalse()
        );
    }

    @Test
    void changeEmpty_groupIdNotNull() {
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(4L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_statusCooking() {
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(2L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_statusMeal() {
        OrderTableUpdateEmptyRequest orderTableUpdateEmptyRequest = new OrderTableUpdateEmptyRequest(false);

        assertThatThrownBy(() -> tableService.changeEmpty(3L, orderTableUpdateEmptyRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(10L);

        OrderTable orderTable = tableService.changeNumberOfGuests(2L, orderTableUpdateGuestsRequest);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @Test
    void changeNumberOfGuests_EmptyFalse() {
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(10L);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableUpdateGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_numberOfGuestNegative() {
        OrderTableUpdateGuestsRequest orderTableUpdateGuestsRequest = new OrderTableUpdateGuestsRequest(-1L);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, orderTableUpdateGuestsRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
