package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.Menu;

public class MenuResult {

    private final Long id;
    private final String name;
    private final Long price;
    private final MenuGroupResult menuGroupResult;

    public MenuResult(
            final Long id,
            final String name,
            final Long price,
            final MenuGroupResult menuGroupResult
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResult = menuGroupResult;
    }

    public static MenuResult from(final Menu menu) {
        return new MenuResult(
                menu.getId(),
                menu.getName(),
                menu.getPrice().getValue().longValue(),
                MenuGroupResult.from(menu.getMenuGroup())
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
}
