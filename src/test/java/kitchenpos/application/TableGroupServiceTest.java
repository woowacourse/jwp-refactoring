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
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.TableGroupRequestDto;
import kitchenpos.table.application.dto.TableGroupResponse;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Nested
    @DisplayName("테이블 그룹핑 테스트")
    class create {

        @Test
        @DisplayName("테이블을 그룹핑한다.")
        void create() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTableResponse savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            final TableGroupResponse actual = tableGroupService.create(tableGroup);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        @DisplayName("그룹핑할 테이블이 비어있으면 예외를 발생시킨다.")
        void create_emptyOrderTable() {
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성();

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블이 두개 미만이면 예외를 발생시킨다.")
        void create_lessThanTwoOrderTable() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성(savedOrderTable1);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 존재하지 않는 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotExistOrderTable() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTableResponse savedOrderTable2 = new OrderTableResponse(0L, null, 0, true);
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("그룹핑할 테이블중 비어있지 않은 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotEmptyOrderTable() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있지_않은_주문_테이블);
            final OrderTableResponse savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable2);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 이미 다른 테이블 그룹애 포함된 테이블을 포함하면 예외를 발생시킨다.")
        void create_containAlreadyGroupTable() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTableResponse savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTableResponse savedOrderTable3 = 주문_테이블_등록(비어있는_주문_테이블);

            테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));
            final TableGroupRequestDto tableGroup = 테이블_그룹_구성(savedOrderTable1, savedOrderTable3);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("그룹핑 해제 테스트")
    class ungroup {

        private TableGroupResponse tableGroup;
        private OrderTableResponse savedOrderTable1;
        private OrderTableResponse savedOrderTable2;

        @BeforeEach
        void setup() {
            savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);

            tableGroup = 테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));
        }

        @Test
        @DisplayName("그룹핑 되어있는 테이블을 분리한다.")
        void ungroup() {
            tableGroupService.ungroup(tableGroup.getId());
            final OrderTable orderTable1 = orderTableRepository.findById(savedOrderTable1.getId()).get();
            final OrderTable orderTable2 = orderTableRepository.findById(savedOrderTable2.getId()).get();

            assertAll(
                    () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                    () -> assertThat(orderTable2.getTableGroupId()).isNull()
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("테이블 상태가 COOKING이거나 MEAL일 경우 예외를 발생시킨다.")
        void ungroup_CookingOrMeal(final String orderStatus) {
            final List<OrderTable> orderTable = tableGroup.getOrderTables();
            final BigDecimal lessThanSingleProductPrice = BigDecimal.valueOf(9000);
            final Product savedProduct = 상품_등록(상품);
            final MenuGroup savedMenuGroup = 메뉴_그룹_등록(메뉴_그룹);
            final MenuResponse savedMenu = 메뉴_등록(메뉴_생성("메뉴이름", lessThanSingleProductPrice, savedMenuGroup.getId(), savedProduct));
            주문_등록(주문_생성(orderTable.get(0).getId(), savedMenu, orderStatus));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
