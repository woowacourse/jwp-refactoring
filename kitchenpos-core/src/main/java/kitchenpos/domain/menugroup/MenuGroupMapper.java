package kitchenpos.domain.menugroup;

import kitchenpos.dto.request.MenuGroupCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public MenuGroup toMenuGroup(final MenuGroupCreateRequest request) {
        return new MenuGroup(request.getName());
    }
}
