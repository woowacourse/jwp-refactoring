package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.application.mapper.MenuMapper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuMapper menuMapper;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, MenuMapper menuMapper,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuMapper = menuMapper;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        Menu menu = menuMapper.mapFrom(menuRequest);
        menu.register(menuValidator);

        menuRepository.save(menu);
        menuProductRepository.saveAll(menu.getMenuProducts().toList());
        return menu;
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(new MenuProducts(menuProductRepository.findAllByMenuId(menu.getId())));
        }

        return menus;
    }
}
