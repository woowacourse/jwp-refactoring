package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.application.dto.MenuGroupResponse;
import kitchenpos.domain.model.MenuGroup;
import kitchenpos.domain.model.MenuGroupRepository;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        return MenuGroupResponse.from(menuGroupRepository.save(request.toEntity()));
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
