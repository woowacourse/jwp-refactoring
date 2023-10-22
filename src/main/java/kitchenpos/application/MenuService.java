package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        validateExistingMenuGroup(request.getMenuGroupId());
        final List<MenuProduct> menuProducts = menuProductRepository.getAllById(request.getMenuProductIds());

        final Menu menu = Menu.forSave(request.getName(), menuProducts);
        return menuRepository.save(menu);
    }

    private void validateExistingMenuGroup(final Long menuGroupId) {
        if (menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
