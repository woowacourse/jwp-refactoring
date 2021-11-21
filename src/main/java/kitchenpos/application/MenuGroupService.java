package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dtos.MenuGroupRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupRequest request) {
        final MenuGroup menuGroup = menuGroupWith(request);
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    private MenuGroup menuGroupWith(MenuGroupRequest request) {
        return MenuGroup.builder()
                .name(request.getName())
                .build();
    }
}
