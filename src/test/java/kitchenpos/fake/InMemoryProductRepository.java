package kitchenpos.fake;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> map = new HashMap<>();
    private AtomicLong id = new AtomicLong(0);

    @Override
    public Product save(Product entity) {
        long id = this.id.getAndIncrement();
        Product product = new Product(id, entity.getName(), entity.getPrice());
        map.put(id, product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> ids) {
        return ids.stream()
                .map(map::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(map.values());
    }
}
