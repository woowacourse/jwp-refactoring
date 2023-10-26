package kitchenpos.application.menu;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.menu.dto.CreateMenuGroupCommand;
import kitchenpos.application.menu.dto.CreateMenuGroupResponse;
import kitchenpos.application.menu.dto.SearchMenuGroupResponse;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public CreateMenuGroupResponse create(CreateMenuGroupCommand command) {
        MenuGroup menuGroup = new MenuGroup(command.name());
        return CreateMenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public List<SearchMenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(SearchMenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}
