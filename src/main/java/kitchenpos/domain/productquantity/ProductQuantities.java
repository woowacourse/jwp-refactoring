package kitchenpos.domain.productquantity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.price.Price;
import kitchenpos.domain.price.Prices;

public class ProductQuantities {

    final List<ProductQuantity> productQuantities;

    public ProductQuantities() {
        productQuantities = new ArrayList<>();
    }

    public void add(ProductQuantity productQuantity) {
        productQuantities.add(productQuantity);
    }

    public void validateTotalPriceIsGreaterOrEqualThan(Price otherPrice) {
        final Price productsTotalPrice = getProductsTotalPrice();
        if (!productsTotalPrice.isGreaterOrEqualThan(otherPrice)) {
            throw new IllegalArgumentException("otherPrice가 productQuantities들의 price합보다 큽니다.");
        }
    }

    private Price getProductsTotalPrice() {
        final Prices prices = new Prices(getProductsTotalPrices());
        return prices.sum();
    }

    private List<Price> getProductsTotalPrices() {
        return productQuantities.stream()
            .map(ProductQuantity::getTotalPrice)
            .collect(Collectors.toList())
            ;
    }

    public List<ProductQuantity> getProductQuantities() {
        return new ArrayList<>(productQuantities);
    }
}
