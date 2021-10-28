package kitchenpos.application;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import static kitchenpos.application.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @BeforeEach
    void setUp() {
        menuGroupService.create(MENU_GROUP);
        productService.create(PRODUCT);
        menuService.create(MENU);

        final OrderTable firstOrderTable = tableService.create(FIRST_ORDER_TABLE);
        final OrderTable secondOrderTable = tableService.create(SECOND_ORDER_TABLE);

        TABLE_GROUP.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
    }

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTest() {

        // when
        final TableGroup tableGroup = tableGroupService.create(TABLE_GROUP);

        // then
        assertThat(tableGroupDao.findById(1L).get()).isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("테이블 등록 해제")
    void ungroupTest() {

        // given
        final TableGroup tableGroup = tableGroupService.create(TABLE_GROUP);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }
}
