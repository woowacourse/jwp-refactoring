package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

  private final MenuRepository menuRepository;
  private final MenuGroupRepository menuGroupRepository;

  public MenuService(
      final MenuRepository menuRepository,
      final MenuGroupRepository menuGroupRepository
  ) {
    this.menuRepository = menuRepository;
    this.menuGroupRepository = menuGroupRepository;
  }

  @Transactional
  public Menu2 create(final Menu2 menu) {
    if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
      throw new IllegalArgumentException();
    }

    final MenuProducts menuProducts = new MenuProducts(menu.getMenuProducts());

    if (menuProducts.isSumLowerThan(menu.getPrice())) {
      throw new IllegalArgumentException();
    }

    return menuRepository.save(menu);
  }

  public List<Menu2> list() {
    return menuRepository.findAll();
  }
}
