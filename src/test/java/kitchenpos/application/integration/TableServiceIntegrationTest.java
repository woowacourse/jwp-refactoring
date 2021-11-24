package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.application.common.factory.OrderTableFactory;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("생성 - 테이블을 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        OrderTable orderTable = OrderTableFactory.create(1, false);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getId()).isNotNull();
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(1);
        assertThat(savedOrderTable.getTableGroupId()).isNull();
    }

    @DisplayName("조회 - 전체 테이블을 조회할 수 있다.")
    @Test
    void list_success() {
        // given
        OrderTable orderTable1 = OrderTableFactory.create(5, false);
        OrderTable orderTable2 = OrderTableFactory.create(0, true);

        // when
        OrderTable savedOrderTable1 = tableService.create(orderTable1);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables)
            .hasSize(2)
            .containsExactlyInAnyOrder(savedOrderTable1, savedOrderTable2);
    }

    @DisplayName("빈 테이블로 변경 - 특정 테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty_success() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(5, false)
        );

        // when
        savedOrderTable.setEmpty(true);
        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블로 변경 - 존재하지 않는 테이블을 빈 테이블로 변경할 수 없다.")
    @Test
    void changeEmpty_nonExists_fail() {
        // given
        OrderTable nonSavedOrderTable = OrderTableFactory.create(1, false);

        // when, then
        nonSavedOrderTable.setEmpty(true);
        assertThatThrownBy(() -> tableService.changeEmpty(nonSavedOrderTable.getId(), nonSavedOrderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 존재하지 않는 테이블을 빈 테이블로 변경할 수 없다.");
    }

    @DisplayName("인원 수 변경 - 특정 테이블의 인원 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(1, false)
        );

        // when
        savedOrderTable.setNumberOfGuests(2);
        OrderTable exchangedOrderTable = tableService
            .changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable);

        // then
        assertThat(exchangedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("인원 수 변경 - 변경하려는 테이블의 인원 수는 0보다 작을 수 없다.")
    @Test
    void changeNumberOfGuests_lessThanZero_fail() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(1, false)
        );

        // when, then
        savedOrderTable.setNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            savedOrderTable.getId(),
            savedOrderTable
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 변경하려는 테이블의 인원 수는 0보다 작을 수 없다.");
    }

    @DisplayName("인원 수 변경 - 변경하려는 테이블이 비어 있다면 인원 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_emptyTable_fail() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(0, true)
        );

        // when, then
        savedOrderTable.setNumberOfGuests(5);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            savedOrderTable.getId(),
            savedOrderTable
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 변경하려는 테이블이 비어 있다면 인원 수를 변경할 수 없다.");
    }

    @DisplayName("인원 수 변경 - 존재하지 않는 테이블의 인원 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_nonExists_fail() {
        // given
        OrderTable nonSavedOrderTable = OrderTableFactory.create(5, false);

        // when, then
        nonSavedOrderTable.setNumberOfGuests(3);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
            nonSavedOrderTable.getId(),
            nonSavedOrderTable
        ))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 존재하지 않는 테이블의 인원 수를 변경할 수 없다.");
    }
}
