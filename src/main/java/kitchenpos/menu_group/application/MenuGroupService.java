package kitchenpos.menu_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu_group.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu_group.application.dto.MenuGroupQueryResponse;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import kitchenpos.menu_group.persistence.MenuGroupRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
