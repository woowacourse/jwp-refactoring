package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.MenuProductRequestDto;
import kitchenpos.application.dto.request.MenuRequestDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.ui.dto.request.MenuRequest;
import kitchenpos.ui.dto.response.MenuProductResponse;
import kitchenpos.ui.dto.response.MenuResponse;

public class MenuAssembler {

    private MenuAssembler() {
    }

    public static MenuRequestDto menuRequestDto(MenuRequest request) {
        List<MenuProductRequestDto> menuProducts = request.getMenuProducts().stream()
            .map(source -> new MenuProductRequestDto(source.getProductId(), source.getQuantity()))
            .collect(toList());

        return new MenuRequestDto(
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            menuProducts
        );
    }

    public static MenuResponse menuResponse(MenuResponseDto responseDto) {
        List<MenuProductResponse> menuProducts = responseDto.getMenuProducts().stream()
            .map(source -> new MenuProductResponse(source.getSeq(), source.getQuantity()))
            .collect(toList());

        return new MenuResponse(
            responseDto.getId(),
            responseDto.getName(),
            responseDto.getPrice(),
            menuProducts
        );
    }

    public static List<MenuResponse> menuResponses(List<MenuResponseDto> menuResponsesDto) {
        return menuResponsesDto.stream()
            .map(MenuAssembler::menuResponse)
            .collect(toList());
    }
}
