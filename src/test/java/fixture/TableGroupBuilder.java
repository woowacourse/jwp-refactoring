//package fixture;
//
//import kitchenpos.tablegroup.domain.TableGroup;
//
//import java.time.LocalDateTime;
//
//public class TableGroupBuilder {
//    private Long id;
//    private LocalDateTime createdDate;
//
//    public static TableGroupBuilder init() {
//        final TableGroupBuilder builder = new TableGroupBuilder();
//        builder.id = null;
//        builder.createdDate = LocalDateTime.now();
//        return builder;
//    }
//
//    public TableGroupBuilder id(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public TableGroupBuilder createdDate(LocalDateTime createdDate) {
//        this.createdDate = createdDate;
//        return this;
//    }
//
//    public TableGroup build() {
//        return new TableGroup(
//                id,
//                createdDate
//        );
//    }
//}
