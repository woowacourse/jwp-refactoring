package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.jpa.MenuGroupRepository;
import kitchenpos.dao.jpa.MenuProductRepository;
import kitchenpos.dao.jpa.MenuRepository;
import kitchenpos.dao.jpa.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.specification.MenuSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductDao;
    private final ProductRepository productRepository;
    private final MenuSpecification menuSpecification;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductDao,
                       ProductRepository productRepository,
                       MenuSpecification menuSpecification) {

        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
        this.menuSpecification = menuSpecification;
    }

    @Transactional
    public Menu create(MenuRequest menuRequest) {

        final Menu menu = menuRequest.toDomain();
        menuSpecification.validateCreate(menu);

        final Menu savedMenu = menuRepository.save(menu);
        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
