package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import lombok.Getter;

@Getter
public class MenuGroupResponses {
    private final List<MenuGroupResponse> menuGroups;

    public MenuGroupResponses(List<MenuGroup> menuGroups) {
        this.menuGroups = menuGroups.stream()
                .map(MenuGroupResponse::new)
                .collect(Collectors.toList());
    }
}
