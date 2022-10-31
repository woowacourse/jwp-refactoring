package kitchenpos.application;

import java.util.List;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.menu.MenuProductRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.menu.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.specification.MenuSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuSpecification menuSpecification;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository,
                       ProductRepository productRepository,
                       MenuSpecification menuSpecification) {

        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.menuSpecification = menuSpecification;
    }

    @Transactional
    public Menu create(MenuRequest menuRequest) {

        /**
         * TODO MenuProduct : Domain to Request
         * menuRequest.productIds();
         * p Repo
         */
        final Menu menu = menuRequest.toDomain();
        menuSpecification.validateCreate(menu);

        final Menu save = menuRepository.save(menu);
        return save;
    }

    public List<Menu> list() {

        final List<Menu> menus = menuRepository.findAllWithMenuProduct();
        return menus;
    }
}
