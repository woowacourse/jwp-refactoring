package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProductCreateInfo;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.PriceValidator;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuResponse;

@Service
public class MenuService {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest menuCreateRequest) {
        Menu menu = createMenu(menuCreateRequest.getName(), menuCreateRequest.getPrice(),
            menuCreateRequest.getMenuGroupId(),
            menuCreateRequest.getMenuProductCreateInfos());

        return MenuResponse.of(menuRepository.save(menu));
    }

    private Menu createMenu(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateInfo> menuProductCreateInfos) {
        validateMenuGroupId(menuGroupId);
        List<Long> productIds = menuProductCreateInfos.stream()
            .map(MenuProductCreateInfo::getProductId)
            .collect(Collectors.toList());
        PriceValidator priceValidator = PriceValidator.of(productRepository.findAllById(productIds));
        priceValidator.validate(price, menuProductCreateInfos);

        return new Menu(name, price, menuGroupId,
            menuProductCreateInfos.stream().map(MenuProductCreateInfo::toMenuProduct).collect(
                Collectors.toList()));
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
