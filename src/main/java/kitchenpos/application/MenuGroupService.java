package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.application.dto.MenuGroupResponse;
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
    public MenuGroupResponse create(final MenuGroupCreateRequest menuGroupCreateRequest) {
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroupCreateRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listOf(menuGroupRepository.findAll());
    }
}
