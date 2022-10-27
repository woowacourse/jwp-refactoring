package kitchenpos.application;

import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.OrderFixture.주문_생성;
import static kitchenpos.support.OrderTableFixture.비어있는_주문_테이블;
import static kitchenpos.support.OrderTableFixture.비어있지_않은_주문_테이블;
import static kitchenpos.support.ProductFixture.상품;
import static kitchenpos.support.TableGroupFixture.테이블_그룹_구성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Nested
    @DisplayName("테이블 그룹핑 테스트")
    class create {

        @Test
        @DisplayName("테이블을 그룹핑한다.")
        void create() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroup tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            final TableGroup actual = tableGroupService.create(tableGroup);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        @DisplayName("그룹핑할 테이블이 비어있으면 예외를 발생시킨다.")
        void create_emptyOrderTable() {
            final TableGroup tableGroup = 테이블_그룹_구성();

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블이 두개 미만이면 예외를 발생시킨다.")
        void create_lessThanTwoOrderTable() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroup tableGroup = 테이블_그룹_구성(savedOrderTable1);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 존재하지 않는 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotExistOrderTable() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable2 = 비어있는_주문_테이블;
            final TableGroup tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("그룹핑할 테이블중 비어있지 않은 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotEmptyOrderTable() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있지_않은_주문_테이블);
            final OrderTable savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroup tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 이미 다른 테이블 그룹애 포함된 테이블을 포함하면 예외를 발생시킨다.")
        void create_containAlreadyGroupTable() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable3 = 주문_테이블_등록(비어있는_주문_테이블);

            테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));
            final TableGroup tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable3);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("그룹핑 해제 테스트")
    class ungroup {

        private TableGroup tableGroup;

        @BeforeEach
        void setup() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);

            tableGroup = 테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));
        }

        @Test
        @DisplayName("그룹핑 되어있는 테이블을 분리한다.")
        void ungroup() {
            tableGroupService.ungroup(tableGroup.getId());
            final List<OrderTable> orderTables = tableGroup.getOrderTables();
            final Optional<OrderTable> firstTable = orderTableDao.findById(orderTables.get(0).getId());
            final Optional<OrderTable> secondTable = orderTableDao.findById(orderTables.get(1).getId());

            assertAll(
                    () -> assertThat(firstTable.get().getTableGroupId()).isNull(),
                    () -> assertThat(secondTable.get().getTableGroupId()).isNull()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("테이블 상태가 COOKING이거나 MEAL일 경우 예외를 발생시킨다.")
        void ungroup_CookingOrMeal(final String orderStatus) {

            final OrderTable orderTable = tableGroup.getOrderTables().get(0);
            final BigDecimal lessThanSingleProductPrice = BigDecimal.valueOf(9000);
            final Product savedProduct = 상품_등록(상품);
            final MenuGroup savedMenuGroup = 메뉴_그룹_등록(메뉴_그룹);
            final Menu savedMenu = 메뉴_등록(메뉴_생성("메뉴이름", lessThanSingleProductPrice, savedMenuGroup.getId(), savedProduct));
            주문_등록(주문_생성(orderTable.getId(), savedMenu, orderStatus));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
