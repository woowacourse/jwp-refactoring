package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "classpath:data/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(0, true);

        // when
        final OrderTable result = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getTableGroupId()).isNull();
        });
    }

    @Test
    void list() {
        // given
        tableService.create(new OrderTable(0, true));
        tableService.create(new OrderTable(0, true));

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));
        final OrderTable changed = new OrderTable(false);

        // when
        final OrderTable result = tableService.changeEmpty(saved.getId(), changed);

        // then
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void changeEmpty_tableNullException() {
        // given
        final OrderTable changed = new OrderTable(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_tableGroupException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));
        saved.setTableGroupId(1L);
        final OrderTable changed = new OrderTable(false);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, false));
        final OrderTable changed = new OrderTable(1);

        // when
        final OrderTable result = tableService.changeNumberOfGuests(saved.getId(), changed);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void changeNumberOfGuests_numberException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, false));
        final OrderTable changed = new OrderTable(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableNullException() {
        // given
        final OrderTable changed = new OrderTable(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableEmptyException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));
        final OrderTable changed = new OrderTable(1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
