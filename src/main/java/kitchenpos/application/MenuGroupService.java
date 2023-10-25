package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuGroupDto;
import kitchenpos.application.dto.ReadMenuGroupDto;
import kitchenpos.domain.menugroup.repository.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.ui.dto.request.CreateMenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public CreateMenuGroupDto create(final CreateMenuGroupRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup persistMenuGroup = menuGroupRepository.save(menuGroup);

        return new CreateMenuGroupDto(persistMenuGroup);
    }

    public List<ReadMenuGroupDto> list() {
        return menuGroupRepository.findAll()
                                  .stream()
                                  .map(ReadMenuGroupDto::new)
                                  .collect(Collectors.toList());
    }
}
