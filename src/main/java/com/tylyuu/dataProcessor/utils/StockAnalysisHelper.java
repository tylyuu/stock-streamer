package com.tylyuu.dataProcessor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.functions;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.mutable.WrappedArray;

import java.util.HashMap;
import java.util.Map;

public class StockAnalysisHelper {

    private static final Logger logger = LoggerFactory.getLogger(StockAnalysisHelper.class);

    public static Dataset<Row> calculateMovingAverage(Dataset<Row> data, int windowSize) {
        return data.withColumn("movingAvg",
                functions.avg(data.col("close"))
                        .over(Window.orderBy("date").rowsBetween(-windowSize + 1, 0)))
                .select(data.col("date"), functions.col("movingAvg"));
    }

    public static Dataset<Row> calculatePriceVariation(Dataset<Row> data) {
        return data.withColumn("priceVariation", data.col("high").minus(data.col("low")))
                .select("date", "priceVariation");
    }

    public static Dataset<Row> analyzeVolume(Dataset<Row> data) {
        return data.agg(functions.avg(data.col("volume")).alias("averageVolume"))
                .select(functions.lit(null).alias("date"), functions.col("averageVolume"));
    }


    public static Dataset<Row> findMaxMinPrices(Dataset<Row> data) {
        Dataset<Row> maxPrice = data.agg(functions.max(data.col("close")).alias("maxClosePrice"));
        Dataset<Row> minPrice = data.agg(functions.min(data.col("close")).alias("minClosePrice"));
        return maxPrice.crossJoin(minPrice)
                .select(functions.lit(null).alias("date"),
                        functions.col("maxClosePrice"),
                        functions.col("minClosePrice"));
    }


    public static Dataset<Row> calculateSimpleReturn(Dataset<Row> data) {
        Dataset<Row> dataWithPreviousClose = data.withColumn("previousClose",
                functions.lag(data.col("close"), 1)
                        .over(Window.orderBy("date")));

        // Calculate the simple return
        return dataWithPreviousClose.withColumn("simpleReturn",
                dataWithPreviousClose.col("close")
                        .minus(dataWithPreviousClose.col("previousClose"))
                        .divide(dataWithPreviousClose.col("previousClose"))
                        .multiply(100))
                .select(data.col("date"), functions.col("simpleReturn"));

    }

    public static String calculateAggregatedMetrics(Dataset<Row> dataset) {
        // Find the starting date (the smallest day in the dataset)
        Row minDateRow = dataset.agg(functions.min(dataset.col("date"))).first();
        WrappedArray<Integer> wrappedDateArray = (WrappedArray<Integer>) minDateRow.getAs(0);

        String year = String.valueOf(wrappedDateArray.apply(0));
        String month = String.format("%02d", wrappedDateArray.apply(1)); // Ensures two digits
        String day = String.format("%02d", wrappedDateArray.apply(2));   // Ensures two digits

        String startDate = year + "-" + month + "-" + day;

        // Calculate averages and min/max
        Dataset<Row> summary = dataset
                .agg(
                        functions.avg(dataset.col("movingAvg")).alias("avgMovingAvg"),
                        functions.avg(dataset.col("priceVariation")).alias("avgPriceVariation"),
                        functions.avg(dataset.col("simpleReturn")).alias("avgSimpleReturn"),
                        functions.avg(dataset.col("volume")).alias("avgVolume"),
                        functions.min(dataset.col("close")).alias("minClose"),
                        functions.max(dataset.col("close")).alias("maxClose")
                );

        // Convert the result to a JSON string
        Map<String, Object> resultMap = new HashMap<>();
        Row summaryRow = summary.first();
     //   resultMap.put("startDate", startDate);
        resultMap.put("avgMovingAvg", summaryRow.getAs("avgMovingAvg"));
        resultMap.put("avgPriceVariation", summaryRow.getAs("avgPriceVariation"));
        resultMap.put("avgSimpleReturn", summaryRow.getAs("avgSimpleReturn"));
        resultMap.put("avgVolume", summaryRow.getAs("avgVolume"));
        resultMap.put("minClose", summaryRow.getAs("minClose"));
        resultMap.put("maxClose", summaryRow.getAs("maxClose"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResult = "";
        try {
            jsonResult = objectMapper.writeValueAsString(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject ret = new JSONObject();
        ret.put(startDate, jsonResult);

        return ret.toString();
    }


    public static Dataset<Row> calculate(Dataset<Row> data, int movingAverageWindowSize) {
        // Calculate each metric
        Dataset<Row> movingAvgData = calculateMovingAverage(data, movingAverageWindowSize);
        Dataset<Row> priceVariationData = calculatePriceVariation(data);
        Dataset<Row> simpleReturnData = calculateSimpleReturn(data);


        Dataset<Row> combinedData = data
                .join(movingAvgData, "date") // Join on the date column
                .join(priceVariationData, "date")

                .join(simpleReturnData, "date")
                .orderBy(functions.col("date").asc());

        return combinedData;
    }
}
