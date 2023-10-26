package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuGroupCreationRequest;
import kitchenpos.menu.application.dto.MenuGroupResult;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResult create(final MenuGroupCreationRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return MenuGroupResult.from(menuGroupRepository.save(menuGroup));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResult> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResult::from)
                .collect(Collectors.toList());
    }
}
