package kitchenpos.dao;

import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class JdbcDaoTest {

    @Autowired
    private DataSource dataSource;

    protected MenuDao menuDao;
    protected MenuGroupDao menuGroupDao;
    protected MenuProductDao menuProductDao;
    protected ProductDao productDao;
    protected TableGroupDao tableGroupDao;

    @BeforeEach
    void setUp() {
        this.menuDao = new JdbcTemplateMenuDao(dataSource);
        this.menuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        this.menuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        this.productDao = new JdbcTemplateProductDao(dataSource);
        this.tableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
    }

    protected Product 상품을_저장한다(final Product product) {
        return productDao.save(product);
    }

    protected MenuGroup 메뉴그룹을_저장한다(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    protected Menu 메뉴를_저장한다(final Menu menu) {
        return menuDao.save(menu);
    }

    protected MenuProduct 메뉴상품을_저장한다(final MenuProduct menuProduct) {
        return menuProductDao.save(menuProduct);
    }

    protected TableGroup 테이블그룹을_저장한다(final TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }
}
