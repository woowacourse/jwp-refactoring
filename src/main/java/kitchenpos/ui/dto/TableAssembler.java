package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.TableEmptyRequestDto;
import kitchenpos.application.dto.request.TableNumberOfGuestsRequestDto;
import kitchenpos.application.dto.request.TableRequestDto;
import kitchenpos.application.dto.response.TableEmptyResponseDto;
import kitchenpos.application.dto.response.TableNumberOfGuestsResponseDto;
import kitchenpos.application.dto.response.TableResponseDto;
import kitchenpos.ui.dto.request.TableEmptyRequest;
import kitchenpos.ui.dto.request.TableNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.TableRequest;
import kitchenpos.ui.dto.response.TableEmptyResponse;
import kitchenpos.ui.dto.response.TableNumberOfGuestsResponse;
import kitchenpos.ui.dto.response.TableResponse;

public class TableAssembler {

    private TableAssembler() {
    }

    public static TableRequestDto tableRequestDto(TableRequest request) {
        return new TableRequestDto(request.getNumberOfGuests(), request.getEmpty());
    }

    public static TableResponse tableResponse(TableResponseDto responseDto) {
        return new TableResponse(responseDto.getId());
    }

    public static List<TableResponse> tableResponses(
        List<TableResponseDto> responsesDto
    ) {
        return responsesDto.stream()
            .map(TableAssembler::tableResponse)
            .collect(toList());
    }

    public static TableEmptyRequestDto tableEmptyRequestDto(
        Long orderTableId,
        TableEmptyRequest request
    ) {
        return new TableEmptyRequestDto(orderTableId, request.getEmpty());
    }

    public static TableEmptyResponse tableEmptyResponse(TableEmptyResponseDto responseDto) {
        return new TableEmptyResponse(responseDto.getEmpty());
    }

    public static TableNumberOfGuestsRequestDto tableNumberOfGuestsRequestDto(
        Long orderTableId,
        TableNumberOfGuestsRequest request
    ) {
        return new TableNumberOfGuestsRequestDto(orderTableId, request.getNumberOfGuests());
    }

    public static TableNumberOfGuestsResponse tableNumberOfGuestsResponse(
        TableNumberOfGuestsResponseDto responseDto
    ) {
        return new TableNumberOfGuestsResponse(responseDto.getNumberOfGuests());
    }
}
