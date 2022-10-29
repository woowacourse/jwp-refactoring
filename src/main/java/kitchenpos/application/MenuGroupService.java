package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.dto.mapper.MenuGroupDtoMapper;
import kitchenpos.dto.mapper.MenuGroupMapper;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupMapper menuGroupMapper;
    private final MenuGroupDtoMapper menuGroupDtoMapper;
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupMapper menuGroupMapper, final MenuGroupDtoMapper menuGroupDtoMapper,
                            final MenuGroupRepository menuGroupRepository) {
        this.menuGroupMapper = menuGroupMapper;
        this.menuGroupDtoMapper = menuGroupDtoMapper;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupMapper.toMenuGroup(menuGroupCreateRequest));
        return menuGroupDtoMapper.toMenuGroupResponse(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDtoMapper.toMenuGroupResponses(menuGroupRepository.findAll());
    }
}
