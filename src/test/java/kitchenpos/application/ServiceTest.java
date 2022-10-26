package kitchenpos.application;

import static kitchenpos.DomainFixture.메뉴_세팅;
import static kitchenpos.DomainFixture.주문_생성;

import java.time.LocalDateTime;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
public class ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    protected Product 상품_저장(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴_그룹_저장(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴_세팅_및_저장(final Menu menu, final long menuGroupId, final long... productIds) {
        return menuDao.save(메뉴_세팅(menu, menuGroupId, productIds));
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
