package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateMenuGroupCommand;
import kitchenpos.application.response.MenuGroupResponse;
import kitchenpos.domain.model.menugroup.MenuGroup;
import kitchenpos.domain.model.menugroup.MenuGroupRepository;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final CreateMenuGroupCommand command) {
        MenuGroup saved = menuGroupRepository.save(command.toEntity());
        return MenuGroupResponse.of(saved);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listOf(menuGroupRepository.findAll());
    }
}
