package kitchenpos.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
        final MenuProductService menuProductService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final Money price = menu.getPrice();

        validPriceIsNullOrMinus(price);
        validMenuGroupIsNotExist(menu.getMenuGroup());

        final MenuProducts menuProducts = menu.getMenuProducts();

        menuProductService.validSumIsLowerThanPrice(price, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        menuProductService.associateMenuProductsAndMenu(menuProducts, savedMenu);

        return savedMenu;
    }

    private void validMenuGroupIsNotExist(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validPriceIsNullOrMinus(Money price) {
        if (Objects.isNull(price) || price.compareTo(Money.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
