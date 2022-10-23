package kitchenpos.ui.dto;

import kitchenpos.application.dto.CreateMenuGroupDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class MenuGroupRequestDto {

    private String name;

    public CreateMenuGroupDto toCreateMenuGroupDto() {
        return new CreateMenuGroupDto(name);
    }
}
