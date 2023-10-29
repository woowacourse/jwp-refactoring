package repository.customRepository;

import domain.Order;
import java.util.List;

public interface CustomOrderRepository {

    List<Order> findAllByFetch();
}
