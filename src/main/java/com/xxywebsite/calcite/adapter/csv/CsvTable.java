package com.xxywebsite.calcite.adapter.csv;

import org.apache.calcite.adapter.file.CsvEnumerator;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rel.type.RelProtoDataType;
import org.apache.calcite.schema.impl.AbstractTable;
import org.apache.calcite.util.Source;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;


public abstract class CsvTable extends AbstractTable {
    protected final Source source;
    protected final @Nullable
    RelProtoDataType protoRowType;
    private @Nullable
    RelDataType rowType;
    private @Nullable List<RelDataType> fieldTypes;

    /**
     * Creates a CsvTable.
     */
    CsvTable(Source source, @Nullable RelProtoDataType protoRowType) {
        this.source = source;
        this.protoRowType = protoRowType;
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory typeFactory) {
        if (protoRowType != null) {
            return protoRowType.apply(typeFactory);
        }
        if (rowType == null) {
            rowType =
                    CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source,
                            null, isStream());
        }
        return rowType;
    }

    /**
     * Returns the field types of this CSV table.
     */
    public List<RelDataType> getFieldTypes(RelDataTypeFactory typeFactory) {
        if (fieldTypes == null) {
            fieldTypes = new ArrayList<>();
            CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source,
                    fieldTypes, isStream());
        }
        return fieldTypes;
    }

    /**
     * Returns whether the table represents a stream.
     */
    protected boolean isStream() {
        return false;
    }

    /**
     * Various degrees of table "intelligence".
     */
    public enum Flavor {
        SCANNABLE, FILTERABLE, TRANSLATABLE
    }
}
