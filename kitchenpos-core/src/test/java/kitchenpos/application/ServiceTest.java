package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.DatabaseClearExtension;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(DatabaseClearExtension.class)
public abstract class ServiceTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

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
        final var order = new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItems));
        return orderRepository.save(order);
    }
}
