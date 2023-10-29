package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.CreateMenuGroupCommand;
import kitchenpos.menu.application.dto.CreateMenuGroupResponse;
import kitchenpos.menu.application.dto.SearchMenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
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
