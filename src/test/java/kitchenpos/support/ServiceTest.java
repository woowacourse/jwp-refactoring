package kitchenpos.support;

import static kitchenpos.support.DomainFixture.주문_생성;

import java.time.LocalDateTime;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    protected Product 상품_저장(final Product product) {
        return productRepository.save(product);
    }

    protected MenuGroup 메뉴_그룹_저장(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu 메뉴_저장(final Menu menu) {
        return menuRepository.save(menu);
    }

    protected OrderTable 테이블_생성_및_저장() {
        final var table = new OrderTable();
        table.setNumberOfGuests(2);
        table.setEmpty(false);
        return orderTableDao.save(table);
    }

    protected OrderTable 빈_테이블_생성_및_저장() {
        final var table = new OrderTable();
        table.setNumberOfGuests(2);
        table.setEmpty(true);
        return orderTableDao.save(table);
    }

    protected Order 주문_생성_및_저장(final long tableId, final long... menuIds) {
        return orderDao.save(주문_생성(tableId, menuIds));
    }

    protected Order 주문_저장(final Order order) {
        return orderDao.save(order);
    }

    protected TableGroup 단체_지정(final OrderTable... orderTables) {
        final var tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final var savedTableGroup = tableGroupDao.save(tableGroup);

        for (final var orderTable : orderTables) {
            orderTable.setTableGroupId(savedTableGroup.getId());
            orderTableDao.save(orderTable);
        }
        return savedTableGroup;
    }
}
