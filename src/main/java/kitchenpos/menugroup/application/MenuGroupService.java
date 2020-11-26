package kitchenpos.menugroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menugroup.model.MenuGroup;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequestDto;
import kitchenpos.menugroup.application.dto.MenuGroupResponseDto;
import kitchenpos.menugroup.repository.MenuGroupRepository;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupCreateRequestDto menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupCreateRequest.toEntity();
        MenuGroup saved = menuGroupRepository.save(menuGroup);
        return MenuGroupResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponseDto.listOf(menuGroups);
    }
}
