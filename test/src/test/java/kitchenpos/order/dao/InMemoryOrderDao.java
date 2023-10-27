package kitchenpos.order.dao;

import domain.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import repository.OrderDao;

public class InMemoryOrderDao implements OrderDao {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public Order save(final Order entity) {
        orders.add(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        return orders.stream()
                     .filter(order -> order.getId().equals(id))
                     .findAny();
    }

    @Override
    public List<Order> findAll() {
        return orders;
    }
}
