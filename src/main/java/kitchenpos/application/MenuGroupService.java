package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.menuGroup.MenuGroupCreateRequest;
import kitchenpos.dto.menuGroup.MenuGroupCreateResponse;
import kitchenpos.dto.menuGroup.MenuGroupFindAllResponses;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupCreateResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupCreateRequest.toEntity());
        return new MenuGroupCreateResponse(menuGroup);
    }

    public MenuGroupFindAllResponses list() {
        return MenuGroupFindAllResponses.from(menuGroupRepository.findAll());
    }
}
