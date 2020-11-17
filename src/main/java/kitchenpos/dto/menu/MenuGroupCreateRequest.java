package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuGroupCreateRequest {
    @NotBlank
    private String name;

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
