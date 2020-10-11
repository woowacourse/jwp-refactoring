package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuGroupResponse {

    private Long id;
    private String name;

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return MenuGroupResponse.builder()
            .id(menuGroup.getId())
            .name(menuGroup.getName())
            .build();
    }
}
