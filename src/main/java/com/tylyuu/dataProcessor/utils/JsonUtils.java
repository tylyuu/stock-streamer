package com.tylyuu.dataProcessor.utils;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;
import org.json.JSONObject;


public class JsonUtils {

    public static Row jsonToRow(String json, StructType schema) {
        JSONObject jsonObject = new JSONObject(json);

        Object[] fields = new Object[schema.fields().length];
        for (int i = 0; i < schema.fields().length; i++) {
            String fieldName = schema.fields()[i].name();
            fields[i] = jsonObject.has(fieldName) ? jsonObject.get(fieldName) : null;
        }

        return RowFactory.create(fields);
    }
}
