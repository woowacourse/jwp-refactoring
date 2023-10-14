package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.OrderTableFixture.테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static kitchenpos.fixture.TableGroupFixture.단체_지정;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService sut;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    private Menu menu;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("피자"));
        Product product = productRepository.save(상품("치즈 피자", 8900L));
        MenuProduct menuProduct = 메뉴_상품(product, 1L);
        menu = menuRepository.save(메뉴("피자", 8900L, menuGroup.getId(), List.of(menuProduct)));
    }

    @Nested
    class 단체_지정을_할_때 {

        @Test
        void 단체_지정하는_테이블이_2개_미만인_경우_예외를_던진다() {
            // given
            OrderTable orderTable = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("단체 지정하려는 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 단체_지정하려는_테이블이_비어있지_않은_경우_예외를_던진다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(테이블(true));
            OrderTable orderTable2 = orderTableRepository.save(테이블(false));
            TableGroupRequest request = 단체_지정_요청(orderTable1, orderTable2);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 이미_단체_지정이_되어있는_테이블을_단체_지정하려는_경우_예외를_던진다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            OrderTable orderTable1 = orderTableRepository.save(테이블(true, 0, tableGroup));
            OrderTable orderTable2 = orderTableRepository.save(테이블(true, 0, tableGroup));
            OrderTable orderTable3 = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable2, orderTable3);

            // expect
            assertThatThrownBy(() -> sut.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비어있지 않거나, 이미 단체 지정이 된 테이블은 단체 지정을 할 수 없습니다.");
        }

        @Test
        void 단체_지정이_성공하는_경우_테이블의_상태가_주문_테이블로_변경된다() {
            // given
            OrderTable orderTable1 = orderTableRepository.save(테이블(true));
            OrderTable orderTable2 = orderTableRepository.save(테이블(true));
            TableGroupRequest request = 단체_지정_요청(orderTable1, orderTable2);

            // when
            TableGroupResponse result = sut.create(request);

            // then
            List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(result.getId());
            assertThat(orderTables)
                    .extracting(OrderTable::isEmpty)
                    .containsExactly(false, false);
        }
    }

    @Nested
    class 단체_지정을_해제_할_때 {

        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "테이블에 해당하는 주문 상태가 {0}인 경우 예외를 던진다")
        void 테이블에_해당하는_주문_상태가_조리중이거나_식사중인_경우_예외를_던진다(OrderStatus orderStatus) {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            OrderTable orderTable1 = orderTableRepository.save(테이블(false, 0, tableGroup));
            OrderTable orderTable2 = orderTableRepository.save(테이블(false, 0, tableGroup));
            OrderLineItem orderLineItem = 주문_항목(menu.getId(), 2);
            orderRepository.save(주문(orderTable1, orderStatus, List.of(orderLineItem)));

            // expect
            assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("테이블의 주문 상태가 조리중이거나 식사중인 경우 단체 지정 해제를 할 수 없습니다.");
        }

        @Test
        void 단체_지정_해제를_성공하는_경우_테이블의_단체_지정_번호가_제거된다() {
            // given
            TableGroup tableGroup = tableGroupRepository.save(단체_지정());
            orderTableRepository.save(테이블(true, 0, tableGroup));
            orderTableRepository.save(테이블(true, 0, tableGroup));

            // when
            sut.ungroup(tableGroup.getId());

            // then
            assertThat(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).isEmpty();
        }
    }
}
