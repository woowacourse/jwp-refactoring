package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final ProductDao productDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    private OrderTable 저장된_주문_테이블1;
    private OrderTable 저장된_주문_테이블2;

    public TableGroupServiceTest(final TableGroupService tableGroupService,
                                 final ProductDao productDao,
                                 final MenuProductDao menuProductDao,
                                 final MenuGroupDao menuGroupDao,
                                 final MenuDao menuDao,
                                 final OrderTableDao orderTableDao,
                                 final OrderDao orderDao) {
        this.tableGroupService = tableGroupService;
        this.productDao = productDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @BeforeEach
    void setUp() {
        final MenuGroup 메뉴_그룹 = 메뉴_그룹(null, "양념 반 후라이드 반");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupDao.save(메뉴_그룹);

        final Product 저장된_양념_치킨 = productDao.save(상품(null, "양념 치킨", BigDecimal.valueOf(12000, 2)));
        final Product 저장된_후라이드_치킨 = productDao.save(상품(null, "후라이드 치킨", BigDecimal.valueOf(10000, 2)));
        final MenuProduct 메뉴_상품_1 = 메뉴_상품(null, null, 저장된_양념_치킨.getId(), 1);
        final MenuProduct 메뉴_상품_2 = 메뉴_상품(null, null, 저장된_후라이드_치킨.getId(), 1);

        final Menu 메뉴 = 메뉴(null, "메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(), List.of(메뉴_상품_1, 메뉴_상품_2));
        final Menu 저장된_메뉴 = menuDao.save(메뉴);

        메뉴_상품_1.setMenuId(저장된_메뉴.getId());
        메뉴_상품_2.setMenuId(저장된_메뉴.getId());
        menuProductDao.save(메뉴_상품_1);
        menuProductDao.save(메뉴_상품_2);

        저장된_주문_테이블1 = orderTableDao.save(주문_테이블(null, null, 2, true));
        저장된_주문_테이블2 = orderTableDao.save(주문_테이블(null, null, 3, true));
    }

    @Nested
    class 테이블_그룹_등록_시 {

        @Test
        void 테이블_그룹을_정상적으로_등록한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));

            // when
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            // then
            assertThat(저장된_테이블_그룹.getOrderTables().get(0).getTableGroupId())
                    .isEqualTo(저장된_테이블_그룹.getId());
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, Collections.emptyList());

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_개수가_2미만이면_예외가_발생한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1));

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력받은_주문_테이블의_개수와_조회한_주문_테이블의_개수가_다르면_예외가_발생한다() {
            // given
            final OrderTable 저장되지_않은_주문_테이블 = 주문_테이블(null, null, 0, true);
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장되지_않은_주문_테이블));

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력받은_주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            final OrderTable 저장된_주문_테이블1 = orderTableDao.save(주문_테이블(null, null, 2, false));
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 입력받은_주문_테이블이_이미_테이블_그룹에_등록되어_있으면_예외가_발생한다() {
            // given
            final OrderTable 저장된_주문_테이블1 = orderTableDao.save(주문_테이블(null, null, 2, true));
            tableGroupService.create(테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2)));

            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));

            // expected
            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_해제_시 {

        @Test
        void 테이블_그룹을_정상적으로_해제한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            // expected
            assertDoesNotThrow(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()));
        }

        @Test
        void 주문_상태가_COOKING인_경우_예외가_발생한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            final Order 주문 = 주문(null, 저장된_주문_테이블1.getId(), null, null, Collections.emptyList());
            주문.setOrderStatus(OrderStatus.COOKING.name());
            주문.setOrderedTime(LocalDateTime.now());
            orderDao.save(주문);

            // expected
            assertThatThrownBy(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_상태가_MEAL인_경우_예외가_발생한다() {
            // given
            final TableGroup 테이블_그룹 = 테이블_그룹(null, null, List.of(저장된_주문_테이블1, 저장된_주문_테이블2));
            final TableGroup 저장된_테이블_그룹 = tableGroupService.create(테이블_그룹);

            final Order 주문 = 주문(null, 저장된_주문_테이블1.getId(), null, null, Collections.emptyList());
            주문.setOrderStatus(OrderStatus.MEAL.name());
            주문.setOrderedTime(LocalDateTime.now());
            orderDao.save(주문);

            // expected
            assertThatThrownBy(() -> tableGroupService.ungroup(저장된_테이블_그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
