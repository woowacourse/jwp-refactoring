package application;

import application.dto.MenuGroupCreateRequest;
import application.dto.MenuGroupResponse;
import domain.MenuGroup;
import domain.MenuGroupName;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MenuGroupRepository;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroupName menuGroupName = new MenuGroupName(request.getName());
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(menuGroupName));
        return MenuGroupResponse.from(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
