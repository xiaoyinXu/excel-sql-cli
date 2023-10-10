package com.xxywebsite;

import com.xxywebsite.calcite.adapter.csv.CsvSchema;
import com.xxywebsite.calcite.adapter.csv.CsvTable;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

import java.io.File;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author xuxiaoyin
 * @since 2023/10/10
 **/
public class ExcelSqlCliMain {
    public static void main(String[] args) throws Exception {
        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        Connection connection =
                DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection =
                connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();


        URI uri = ExcelSqlCliMain.class.getClassLoader().getResource("csv").toURI();
        File fileDir = new File(uri);
        CsvSchema csvSchema = new CsvSchema(fileDir, CsvTable.Flavor.SCANNABLE);
        rootSchema.add("csv", csvSchema);

        Statement statement = calciteConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM csv.student s JOIN csv.grade g ON s.id = g.student_id");
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            List<String> columnValues = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                columnValues.add(resultSet.getString(i + 1));
            }
            System.out.println(String.join(", ", columnValues));
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
