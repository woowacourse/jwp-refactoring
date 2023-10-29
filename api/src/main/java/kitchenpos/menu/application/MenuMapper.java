package kitchenpos.menu.application;

import kitchenpos.product.application.dto.ProductEventDto;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProductRepository;
import kitchenpos.menu.MenuRepository;
import kitchenpos.product.Price;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ApplicationEventPublisher publisher;

    public MenuMapper(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ApplicationEventPublisher publisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.publisher = publisher;
    }

    public Menu toMenu(final MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final Menu menu = menuRepository.save(new Menu(request.getName(), Price.of(request.getPrice()), menuGroup));

        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            final ProductEventDto productEventDto = new ProductEventDto();
            productEventDto.setId(menuProduct.getProductId());

            publisher.publishEvent(productEventDto);

            menuProductRepository.save(
                    new MenuProduct(
                            menu,
                            productEventDto.getName(),
                            Price.of(productEventDto.getPrice()),
                            menuProduct.getQuantity()
                    )
            );
        }
        return menu;
    }
}
