package kitchenpos.menugroup.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupSaveRequest;
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
    public MenuGroupResponse create(final MenuGroupSaveRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(request.toEntity());
        return new MenuGroupResponse(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::new)
                .collect(toList());
    }
}
