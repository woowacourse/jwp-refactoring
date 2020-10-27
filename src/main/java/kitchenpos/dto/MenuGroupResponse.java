package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<MenuGroupResponse> listFrom(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return MenuGroupResponse.builder()
            .id(menuGroup.getId())
            .name(menuGroup.getName())
            .build();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
