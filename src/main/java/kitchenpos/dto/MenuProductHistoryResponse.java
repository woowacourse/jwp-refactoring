package kitchenpos.dto;

import kitchenpos.menu.domain.MenuProductHistories;
import kitchenpos.menu.domain.MenuProductHistory;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductHistoryResponse {

    private long menuProductHistoryId;
    private ProductHistoryResponse product;
    private long quantity;


    public MenuProductHistoryResponse(final long menuProductHistoryId,
                                      final ProductHistoryResponse product,
                                      final long quantity
    ) {
        this.menuProductHistoryId = menuProductHistoryId;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductHistoryResponse from(final MenuProductHistory menuProductHistory) {
        return new MenuProductHistoryResponse(
                menuProductHistory.getSeq(),
                ProductHistoryResponse.from(menuProductHistory),
                menuProductHistory.getQuantity().getValue()
        );
    }

    public static List<MenuProductHistoryResponse> from(final MenuProductHistories menuProductHistories) {
        return menuProductHistories.getMenuProductHistories()
                .stream()
                .map(MenuProductHistoryResponse::from)
                .collect(Collectors.toList());
    }

    public long getMenuProductHistoryId() {
        return menuProductHistoryId;
    }

    public ProductHistoryResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    private static class ProductHistoryResponse {

        private String name;
        private String price;

        public ProductHistoryResponse(final String name, final String price) {
            this.name = name;
            this.price = price;
        }

        public static ProductHistoryResponse from(final MenuProductHistory menuProductHistory) {
            return new ProductHistoryResponse(menuProductHistory.getName().getValue(), menuProductHistory.getPrice().getValue().toPlainString());
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
    }
}
