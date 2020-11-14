package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        return MenuGroupResponse.of(menuGroupRepository.save(request.toEntity()));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listOf(menuGroupRepository.findAll());
    }
}
