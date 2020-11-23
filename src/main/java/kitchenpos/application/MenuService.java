package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Money;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuCreateResponse;
import kitchenpos.dto.menu.MenuFindAllResponses;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;

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
    public MenuCreateResponse create(final MenuCreateRequest menuCreateRequest) {
        Menu menu = menuCreateRequest.toEntity();
        final Money price = menu.getPrice();

        validPriceIsNullOrMinus(price);
        validMenuGroupIsNotExist(menu.getMenuGroupId());

        final MenuProducts menuProducts = menu.getMenuProducts();

        menuProductService.validSumIsLowerThanPrice(price, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        menuProductService.associateMenuProductsAndMenu(menuProducts, savedMenu);

        return new MenuCreateResponse(savedMenu);
    }

    private void validMenuGroupIsNotExist(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validPriceIsNullOrMinus(Money price) {
        if (price.getValue() == null || price.isMinus()) {
            throw new IllegalArgumentException();
        }
    }

    public MenuFindAllResponses findAll() {
        return MenuFindAllResponses.from(menuRepository.findAll());
    }
}
