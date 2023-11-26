package com.tylyuu.dataProcessor.utils;

import org.apache.spark.sql.types.*;

public class MessageSchema {

    public static StructType getSchema() {
        // Define MetaData schema
        StructType metaDataSchema = new StructType(new StructField[]{
                new StructField("information", DataTypes.StringType, true, Metadata.empty()),
                new StructField("symbol", DataTypes.StringType, true, Metadata.empty()),
                new StructField("lastRefreshed", DataTypes.StringType, true, Metadata.empty()),
                new StructField("timeZone", DataTypes.StringType, true, Metadata.empty()),
                new StructField("interval", DataTypes.StringType, true, Metadata.empty()),
                new StructField("outputSize", DataTypes.StringType, true, Metadata.empty())
        });

        // Define StockUnit schema
        StructType stockUnitSchema = new StructType(new StructField[]{
                new StructField("open", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("high", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("low", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("close", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("adjustedClose", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("volume", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("dividendAmount", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("splitCoefficient", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("date", DataTypes.StringType, true, Metadata.empty())
        });

        // Define Message schema
        return new StructType(new StructField[]{
                new StructField("metaData", metaDataSchema, true, Metadata.empty()),
                new StructField("stockUnits", DataTypes.createArrayType(stockUnitSchema), true, Metadata.empty()),
                new StructField("errorMessage", DataTypes.StringType, true, Metadata.empty())
        });
    }
}