package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = createMenuGroupByRequest(request);
        return menuGroupRepository.save(menuGroup);
    }

    private MenuGroup createMenuGroupByRequest(final MenuGroupCreateRequest request) {
        return new MenuGroup(request.getName());
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
