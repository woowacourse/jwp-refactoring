package kitchenpos.menugroup.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menugroup.ui.dto.request.MenuGroupRequestDto;
import kitchenpos.menugroup.ui.dto.response.MenuGroupResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupRequestDto menuGroupDto) {
        MenuGroup created = menuGroupRepository.save(new MenuGroup(menuGroupDto.getName()));

        return new MenuGroupResponseDto(created.getId(), created.getName());
    }

    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
            .map(menuGroup -> new MenuGroupResponseDto(menuGroup.getId(), menuGroup.getName()))
            .collect(toList());
    }
}
