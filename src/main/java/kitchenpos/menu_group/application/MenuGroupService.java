package kitchenpos.menu_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu_group.application.dto.MenuGroupCreateRequest;
import kitchenpos.menu_group.application.dto.MenuGroupQueryResponse;
import kitchenpos.menu_group.persistence.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupService(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  @Transactional
  public MenuGroupQueryResponse create(final MenuGroupCreateRequest menuGroup) {
    return MenuGroupQueryResponse.from(menuGroupDao.save(menuGroup.toMenuGroup()));
  }

  public List<MenuGroupQueryResponse> list() {
    return menuGroupDao.findAll().stream()
        .map(MenuGroupQueryResponse::from)
        .collect(Collectors.toList());
  }
}
