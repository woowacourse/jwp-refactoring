package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableRepository;
import kitchenpos.dto.NumberOfGuestsChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyChangeRequest;
import kitchenpos.dto.TableResponse;

@ServiceTest
class TableServiceTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 추가한다.")
    @Test
    void create() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, true);
        TableResponse actual = tableService.create(tableCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(TableResponse::getId).isNotNull(),
            () -> assertThat(actual).extracting(TableResponse::getTableGroupId).isNull(),
            () -> assertThat(actual).extracting(TableResponse::getNumberOfGuests).isEqualTo(
                tableCreateRequest.getNumberOfGuests()),
            () -> assertThat(actual).extracting(TableResponse::isEmpty, BOOLEAN).isEqualTo(tableCreateRequest.isEmpty())
        );
    }

    @DisplayName("전체 테이블 목록을 조회한다.")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable(1, true);

        tableRepository.save(orderTable);

        List<TableResponse> actual = tableService.list();

        assertThat(actual).hasSize(1);
    }

    @DisplayName("테이블을 비우거나 채울 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable savedTable = tableRepository.save(new OrderTable(1, false));
        TableEmptyChangeRequest tableEmptyChangeRequest = new TableEmptyChangeRequest(true);

        TableResponse actual = tableService.changeEmpty(savedTable.getId(), tableEmptyChangeRequest);

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비우거나 채울 때 해당되는 테이블 번호가 없다면 예외 처리한다.")
    @Test
    void changeEmptyWithNotExistingTableId() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new TableEmptyChangeRequest(true)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 현재 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedTable = tableRepository.save(new OrderTable(5, false));
        NumberOfGuestsChangeRequest numberOfGuestsChangeRequest = new NumberOfGuestsChangeRequest(4);

        TableResponse actual = tableService.changeNumberOfGuests(savedTable.getId(),
            numberOfGuestsChangeRequest);

        assertThat(actual).extracting(TableResponse::getNumberOfGuests)
            .isEqualTo(numberOfGuestsChangeRequest.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 변경할 시 존재하지 않는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNotExistingTableId() {
        NumberOfGuestsChangeRequest numberOfGuestsChangeRequest = new NumberOfGuestsChangeRequest(4);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, numberOfGuestsChangeRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}