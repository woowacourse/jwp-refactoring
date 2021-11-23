package kitchenpos.application.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.response.MenuProductResponseDto;
import kitchenpos.application.dto.response.MenuResponseDto;
import kitchenpos.domain.Menu;

public class MenuDtoAssembler {

    private MenuDtoAssembler() {
    }

    public static MenuResponseDto menuResponseDto(Menu menu) {
        List<MenuProductResponseDto> menuProducts = menu.getMenuProducts().stream()
            .map(source -> new MenuProductResponseDto(source.getSeq(), source.getQuantity()))
            .collect(toList());

        return new MenuResponseDto(menu.getId(), menu.getName(), menu.getPrice(), menuProducts);
    }
}
