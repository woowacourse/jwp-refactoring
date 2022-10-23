package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateMenuGroupDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MenuGroupRequestDto {

    private String name;

    public CreateMenuGroupDto toCreateMenuGroupDto() {
        return new CreateMenuGroupDto(name);
    }
}
