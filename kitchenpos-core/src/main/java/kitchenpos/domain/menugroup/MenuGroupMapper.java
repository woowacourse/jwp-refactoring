package kitchenpos.domain.menugroup;

import kitchenpos.dto.request.CreateMenuGroupRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupMapper {
    private MenuGroupMapper() {
    }

    public MenuGroup toMenuGroup(final CreateMenuGroupRequest menuGroup) {
        return MenuGroup.builder()
                .name(menuGroup.getName())
                .build();
    }
}
