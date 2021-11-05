package udf;

import org.apache.calcite.rel.core.Calc;
import org.apache.flink.table.functions.AggregateFunction;

/**
 * @Author: logan.yang
 * @Date: 2021/07/09/9:43 上午
 * @Description: No Description
 */

public class UDAFSum extends AggregateFunction<Integer, UDAFSum.SumAccumulator> {
    /**
     * 定义一个Accumulator，存放聚合的中间结果
     */
    public static class SumAccumulator{
        public Integer sumPrice = 0;
    }


    /**
     * 获取聚合结果
     */
    @Override
    public Integer getValue(SumAccumulator sacc) {
        return sacc.sumPrice;
    }

    /**
     * 初始化
     */
    @Override
    public SumAccumulator createAccumulator() {
        return new SumAccumulator();
    }

    /**
     * 定义根据输入更新accumulate的逻辑
     */
    public void accumulate(SumAccumulator sacc, int value){
        sacc.sumPrice += value;
    }
}
