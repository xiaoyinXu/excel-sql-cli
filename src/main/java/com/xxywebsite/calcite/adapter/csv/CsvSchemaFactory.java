package com.xxywebsite.calcite.adapter.csv;

/**
 * @author xuxiaoyin
 * @since 2023/10/10
 **/

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public class CsvSchemaFactory implements SchemaFactory {
    /**
     * Public singleton, per factory contract.
     */
    public static final CsvSchemaFactory INSTANCE = new CsvSchemaFactory();

    private CsvSchemaFactory() {
    }

    @Override
    public Schema create(SchemaPlus parentSchema, String name,
                         Map<String, Object> operand) {
        final String directory = (String) operand.get("directory");
        final File base =
                (File) operand.get(ModelHandler.ExtraOperand.BASE_DIRECTORY.camelName);
        File directoryFile = new File(directory);
        if (base != null && !directoryFile.isAbsolute()) {
            directoryFile = new File(base, directory);
        }
        String flavorName = (String) operand.get("flavor");
        CsvTable.Flavor flavor;
        if (flavorName == null) {
            flavor = CsvTable.Flavor.SCANNABLE;
        } else {
            flavor = CsvTable.Flavor.valueOf(flavorName.toUpperCase(Locale.ROOT));
        }
        return new CsvSchema(directoryFile, flavor);
    }
}

