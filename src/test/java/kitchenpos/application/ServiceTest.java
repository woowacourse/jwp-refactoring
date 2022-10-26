package kitchenpos.application;

import static kitchenpos.Fixture.메뉴;
import static kitchenpos.Fixture.메뉴의_상품은;
import static kitchenpos.Fixture.메뉴집합;
import static kitchenpos.Fixture.상품의_가격은;
import static kitchenpos.Fixture.주문;
import static kitchenpos.Fixture.테이블;
import static kitchenpos.Fixture.테이블집합;

import java.math.BigDecimal;
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

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
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
    private OrderDao orderDao;
    @Autowired
    private TableGroupDao tableGroupDao;

    public Product 상품_생성(int price) {
        return productDao.save(상품의_가격은(new BigDecimal(price)));
    }

    public MenuGroup 메뉴집합_생성() {
        return menuGroupDao.save(메뉴집합());
    }

    public Menu 메뉴_생성() {
        return menuDao.save(메뉴(메뉴집합_생성(), 메뉴의_상품은(상품_생성(100_000)), 메뉴의_상품은(상품_생성(100_000))));
    }

    public OrderTable 테이블_생성(boolean empty) {
        return orderTableDao.save(테이블(empty));
    }

    public Order 주문_생성() {
        return orderDao.save(주문(테이블_생성(false).getId(), 메뉴_생성().getId()));
    }

    public Order 주문_생성(Long tableId) {
        return orderDao.save(주문(tableId, 메뉴_생성().getId()));
    }

    public TableGroup 테이블집합_생성() {
        return tableGroupDao.save(테이블집합(테이블_생성(true), 테이블_생성(true)));
    }
}
