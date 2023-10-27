package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.menugroup.application.mapper.MenuGroupMapper;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.respository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = MenuGroupMapper.toMenuGroup(request);
        final MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        return MenuGroupMapper.toMenuGroupResponse(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> readAll() {
        final List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        return MenuGroupMapper.toMenuGroupResponses(menuGroups);
    }
}
