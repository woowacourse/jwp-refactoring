package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyChangeRequest;
import kitchenpos.dto.request.TableGuestChangeRequest;
import kitchenpos.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        TableCreateRequest orderTable = new TableCreateRequest(0, true);

        TableResponse created = tableService.create(orderTable);

        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("테이블 목록을 불러온다.")
    @Test
    void list() {
        TableCreateRequest orderTable = new TableCreateRequest(0, true);

        tableService.create(orderTable);
        tableService.create(orderTable);

        List<TableResponse> orderTables = tableService.list();
        assertThat(orderTables.size()).isEqualTo(2);
    }

    @DisplayName("테이블이 비었는지 유무를 변경한다.")
    @ParameterizedTest
    @CsvSource({"true,false", "false, true"})
    void changeEmpty(boolean input, boolean expected) {
        TableCreateRequest orderTable = new TableCreateRequest(0, input);
        TableResponse savedOrderTable = tableService.create(orderTable);

        TableEmptyChangeRequest request = new TableEmptyChangeRequest(expected);
        TableResponse response = tableService.changeEmpty(savedOrderTable.getId(), request);

        assertThat(response.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        TableCreateRequest orderTable = new TableCreateRequest(1, false);
        TableResponse savedOrderTable = tableService.create(orderTable);

        TableGuestChangeRequest request = new TableGuestChangeRequest(3);
        TableResponse response = tableService
            .changeNumberOfGuests(savedOrderTable.getId(), request);

        assertAll(
            () -> assertThat(response.getId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(response.getNumberOfGuests()).isEqualTo(3),
            () -> assertThat(response.isEmpty()).isFalse()
        );
    }
}