package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateDto;
import kitchenpos.dto.MenuGroupDto;
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
    public MenuGroupDto create(final MenuGroupCreateDto request) {
        final MenuGroup menugroup = menuGroupRepository.save(request.toDomain());

        return MenuGroupDto.toDto(menugroup);
    }

    public List<MenuGroupDto> list() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return menuGroups.stream()
                .map(MenuGroupDto::toDto)
                .collect(Collectors.toList());
    }
}
