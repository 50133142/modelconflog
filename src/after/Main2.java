package after;

import java.util.*;
import java.util.stream.Collectors;

public class Main2 {
    public static void main(String[] args) {
        Main2 main = new Main2();
        long start = System.currentTimeMillis();
        System.out.println("start...");
        main.mergeRelationLogs2();
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start) / 1000.0F + "s");
        System.out.println("relationLogs size = " + Main2.relationLogs.size());
    }

    public static LinkedList<RelationLog2> relationLogs = new LinkedList<>();

    static {
        //init
        System.out.println("init...");
        int size = 5_000_000;
        for (int i = 0; i < size; i++) {
            relationLogs.add(RelationLog2.create());
        }
    }
    /**
    *  relationLogs.stream()：这一行将 relationLogs 转换成一个流，以便进行后续的流式操作。流是 Java 8 引入的一种用于处理数据集合的方式，可以进行各种操作，如过滤、映射、分组等。

      collect(Collectors.groupingBy(RelationLog2::groupValue))：这一行使用 Collectors.groupingBy 操作，根据 RelationLog2 对象的 groupValue 属性对流中的元素进行分组。groupValue 属性的返回值将用作分组的键，结果是一个 Map，其中键是分组值，值是具有相同分组值的 RelationLog2 对象的列表。换句话说，这一步将 relationLogs 中的元素按照 groupValue 分成了不同的组。

     .values().stream()：这一行从分组的 Map 中提取所有的分组值（Map 的值部分），并将其转换为一个流。现在，我们有了一个流，其中的每个元素都是一个分组，每个分组包含具有相同分组值的 RelationLog2 对象的列表。

     .peek(this::dropSameItem)：这一行使用 peek 操作，对每个分组应用 this::dropSameItem 方法。peek 操作用于在流中的每个元素上执行指定的操作，而不会更改流的内容。在这里，dropSameItem 方法用于处理每个分组，可能是去除相同的元素或进行其他操作。

     flatMap(List::stream)：这一行使用 flatMap 操作，将每个分组中的列表扁平化为一个单一的流。换句话说，它将所有分组中的元素合并成一个大的流，不再分组的概念。

     collect(Collectors.toCollection(LinkedList::new))：最后一行使用 collect 操作，将扁平化的流收集到一个新的 LinkedList 集合中。这一步最终生成了一个包含所有 relationLogs 元素的 LinkedList 集合。

     总结来说，这段代码的目标是对 relationLogs 集合进行分组、去重，然后将结果存储在一个新的 LinkedList 中。这种操作通常用于数据处理中，以便对数据进行分组、去重和重新组织。
    */
    

    private void mergeRelationLogs2() {
        relationLogs = relationLogs.stream()
                .collect(Collectors.groupingBy(RelationLog2::groupValue))
                .values()
                .stream()
                .peek(this::dropSameItem)
                .flatMap(List::stream)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void dropSameItem(List<RelationLog2> list) {
        out: for (int i = 0; i < list.size() - 1; i++) {
            RelationLog2 outRelationLog2 = list.get(i);
            for (int j = i + 1; j < list.size(); j++) {
                RelationLog2 inRelationLog2 = list.get(j);
                if (outRelationLog2.isReversedLog(inRelationLog2)){
                    list.remove(j);
                    list.remove(i);
                    i--;
                    continue out;
                }
            }
        }
    }
}
