package kitchenpos.menu.application;

import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.MenuValidator;
import kitchenpos.menu.ui.MenuRequest;
import kitchenpos.menu.ui.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다. 메뉴를 등록할 수 없습니다."));
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        final Menu savedMenu = menuRepository.save(menu);
        savedMenu.registerMenuProducts(menuValidator, menuRequest.getMenuProducts(), menuRequest.getPrice());

        return MenuResponse.from(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
