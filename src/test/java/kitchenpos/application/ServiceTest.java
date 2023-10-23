package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class ServiceTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected OrderRepository orderRepository;

    protected void 복수_상품_저장(final Product... products) {
        productRepository.saveAll(List.of(products));
    }

    protected MenuGroup 단일_메뉴그룹_저장(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu 단일_메뉴_저장(final Menu menu) {
        return menuRepository.save(menu);
    }

    protected OrderTable 단일_주문테이블_저장(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    protected List<OrderTable> 복수_주문테이블_저장(final OrderTable... orderTables) {
        return orderTableRepository.saveAll(List.of(orderTables));
    }

    protected TableGroup 단일_단체지정_저장(final TableGroup tableGroup) {
        return tableGroupRepository.save(tableGroup);
    }

    protected Order 단일_주문_저장(final OrderTable orderTable, final OrderLineItem... orderLineItems) {
        final var order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItems));
        return orderRepository.save(order);
    }
}
