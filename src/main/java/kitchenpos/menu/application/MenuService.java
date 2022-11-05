package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.PendingMenuProducts;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.ui.request.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final PendingMenuProductsCreator pendingMenuProductsCreator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final PendingMenuProductsCreator pendingMenuProductsCreator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.pendingMenuProductsCreator = pendingMenuProductsCreator;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final PendingMenuProducts products = pendingMenuProductsCreator.create(request);
        final Menu menu = Menu.create(request.getName(), request.getPrice(), products, menuGroup.getId());
        return menuRepository.save(menu);
    }

    @Transactional
    public Menu findById(final long id) {
        return menuRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
