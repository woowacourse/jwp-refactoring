package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuRequestAssembler;
import kitchenpos.menu.application.dto.request.menugroup.MenuGroupRequest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.dto.response.MenuResponseAssembler;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRequestAssembler requestAssembler;
    private final MenuResponseAssembler responseAssembler;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository,
                            final MenuRequestAssembler requestAssembler,
                            final MenuResponseAssembler responseAssembler
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        final var menuGroup = requestAssembler.asMenuGroup(request);
        final var savedMenuGroup = menuGroupRepository.save(menuGroup);
        return responseAssembler.asMenuGroupResponse(savedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        final var menuGroups = menuGroupRepository.findAll();
        return responseAssembler.asMenuGroupResponses(menuGroups);
    }
}
