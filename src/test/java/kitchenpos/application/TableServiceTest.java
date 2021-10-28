package kitchenpos.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;

import static kitchenpos.application.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        menuGroupService.create(MENU_GROUP);
        productService.create(PRODUCT);
        menuService.create(MENU);
    }

    @Test
    @DisplayName("테이블 생성")
    void createTest() {

        // when
        final OrderTable orderTable = tableService.create(FIRST_ORDER_TABLE);

        // then
        assertThat(orderTableDao.findById(1L).get()).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블 목록 조회")
    void listTest() {

        // given
        final OrderTable orderTable = tableService.create(FIRST_ORDER_TABLE);

        // when
        final List<OrderTable> tables = tableService.list();

        // then
        assertThat(tables).contains(orderTable);
    }

    @Test
    @DisplayName("현재 테이블의 빈테이블 상태를 다른 테이블의 빈테이블 상태로 변경")
    void changeEmptyTest() {

        // given
        final OrderTable fromOrderTable = tableService.create(FIRST_ORDER_TABLE);
        final OrderTable toOrderTable = tableService.create(SECOND_ORDER_TABLE);
        toOrderTable.setEmpty(!fromOrderTable.isEmpty());

        // when
        final OrderTable changeOrderTable = tableService.changeEmpty(fromOrderTable.getId(), toOrderTable);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(toOrderTable.isEmpty());
    }
}
