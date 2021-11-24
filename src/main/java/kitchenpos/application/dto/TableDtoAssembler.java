package kitchenpos.application.dto;

import kitchenpos.application.dto.response.TableEmptyResponseDto;
import kitchenpos.application.dto.response.TableNumberOfGuestsResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.domain.OrderTable;

public class TableDtoAssembler {

    private TableDtoAssembler() {
    }

    public static TableResponseDto tableResponseDto(OrderTable orderTable) {
        return new TableResponseDto(orderTable.getId());
    }

    public static TableEmptyResponseDto tableEmptyResponseDto(OrderTable orderTable) {
        return new TableEmptyResponseDto(orderTable.getEmpty());
    }

    public static TableNumberOfGuestsResponseDto tableNumberOfGuestsResponseDto(
        OrderTable orderTable
    ) {
        return new TableNumberOfGuestsResponseDto(orderTable.getNumberOfGuests());
    }
}
