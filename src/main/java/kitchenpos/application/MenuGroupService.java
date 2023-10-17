package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupCreationRequest;
import kitchenpos.application.dto.result.MenuGroupResult;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResult create(final MenuGroupCreationRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        return MenuGroupResult.from(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResult> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResult::from)
                .collect(Collectors.toList());
    }
}
