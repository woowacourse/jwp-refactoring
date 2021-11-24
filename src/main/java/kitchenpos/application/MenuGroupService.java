package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.MenuGroupDtoAssembler;
import kitchenpos.application.dto.request.MenuGroupRequestDto;
import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponseDto create(MenuGroupRequestDto requestDto) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(requestDto.getName()));

        return MenuGroupDtoAssembler.menuGroupResponseDto(menuGroup);
    }

    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
            .map(MenuGroupDtoAssembler::menuGroupResponseDto)
            .collect(toList());
    }
}
