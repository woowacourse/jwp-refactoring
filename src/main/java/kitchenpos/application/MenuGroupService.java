package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuGroupDto;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupDto create(final MenuGroupDto menuGroupDto) {
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupDto.toEntity());
        return MenuGroupDto.from(savedMenuGroup);
    }

    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
            .stream()
            .map(MenuGroupDto::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
