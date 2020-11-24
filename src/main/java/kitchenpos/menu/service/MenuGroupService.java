package kitchenpos.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupCreateRequest;
import kitchenpos.menu.dto.MenuGroupResponses;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public Long create(final MenuGroupCreateRequest request) {
        MenuGroup menuGroup = request.toEntity();
        return menuGroupRepository.save(menuGroup)
            .getId();
    }

    public MenuGroupResponses list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponses.from(menuGroups);
    }
}
