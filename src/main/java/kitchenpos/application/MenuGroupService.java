package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    @Transactional
    public MenuGroupResponse createWithRequest(final MenuGroupRequest menuGroupRequest) {
        return null;
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    public List<MenuGroupResponse> listToMenuGroupResponse() {
        return null;
    }
}
