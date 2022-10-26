package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupDto;
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
        MenuGroup menuGroup = new MenuGroup(menuGroupDto.getName());
        MenuGroup saved = menuGroupRepository.save(menuGroup);
        return new MenuGroupDto(saved);
    }

    public List<MenuGroupDto> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupDto::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
