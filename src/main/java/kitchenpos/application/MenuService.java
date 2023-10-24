package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuCommand;
import kitchenpos.application.dto.CreateMenuCommand.CreateMenuProductCommand;
import kitchenpos.application.dto.domain.MenuDto;
import kitchenpos.domain.Money;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toMap;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(final CreateMenuCommand command) {
        if (!menuGroupRepository.existsById(command.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final Map<Product, Integer> productToQuantity = findProductWithQuantity(command.getMenuProducts());
        final Menu menu = Menu.of(command.getName(), new Money(command.getPrice()), command.getMenuGroupId(),
                productToQuantity);
        return MenuDto.from(menuRepository.save(menu));
    }

    private Map<Product, Integer> findProductWithQuantity(final List<CreateMenuProductCommand> menuProductCommands) {
        return menuProductCommands.stream()
                .collect(toMap(
                        productCommand -> productRepository.getById(productCommand.getProductId()),
                        CreateMenuProductCommand::getQuantity
                ));
    }

    public List<MenuDto> list() {
        return menuRepository.findAll().stream()
                .map(MenuDto::from)
                .collect(Collectors.toList());
    }

}
