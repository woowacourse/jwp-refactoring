package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotBlank;

import kitchenpos.domain.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties("name"))
@Getter
public class MenuGroupCreateRequest {
    @NotBlank
    private final String name;

    public MenuGroup toRequestEntity() {
        return MenuGroup.builder()
            .name(name)
            .build();
    }
}
