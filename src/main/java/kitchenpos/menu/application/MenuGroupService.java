package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.menu.application.dto.response.MenuGroupQueryResponse;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.persistence.MenuGroupRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepositoryImpl menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupQueryResponse create(final MenuGroupCreateRequest menuGroup) {
        return MenuGroupQueryResponse.from(menuGroupRepository.save(menuGroup.toMenuGroup()));
    }

    public List<MenuGroupQueryResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupQueryResponse::from)
                .collect(Collectors.toList());
    }
}
