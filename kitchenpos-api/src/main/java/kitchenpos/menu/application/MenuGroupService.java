package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.application.dto.request.MenuGroupCommand;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupCommand menuGroupCommand) {
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupCommand.toEntity());
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::from)
                .toList();
    }
}
