package kitchenpos.application.dto.result;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResult {

    private final Long id;
    private final String name;
    private final Long price;
    private final MenuGroupResult menuGroupResult;
    private final List<ProductInMenuResult> productInMenuResults;

    public MenuResult(
            final Long id,
            final String name,
            final Long price,
            final MenuGroupResult menuGroupResult,
            final List<ProductInMenuResult> productInMenuResults
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResult = menuGroupResult;
        this.productInMenuResults = productInMenuResults;
    }

    public static MenuResult from(final Menu menu) {
        return new MenuResult(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue().longValue(),
                MenuGroupResult.from(menu.getMenuGroup()),
                menu.getMenuProducts().stream()
                        .map(ProductInMenuResult::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public MenuGroupResult getMenuGroupResult() {
        return menuGroupResult;
    }

    public List<ProductInMenuResult> getMenuProductResults() {
        return productInMenuResults;
    }
}
