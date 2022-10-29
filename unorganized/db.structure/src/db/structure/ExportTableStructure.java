/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure;

import db.structure.table.ColumnInfo;
import db.structure.table.PrimaryKeyInfo;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportTableStructure {

    public static void write(Require require, Path path, String tableName) {
        Document document = buildXmlDocument();
        Element tableTag = document.createElement("Table");
        document.appendChild(tableTag);

        Element tableNameTag = document.createElement("Name");
        tableNameTag.appendChild(document.createTextNode(tableName));
        tableTag.appendChild(tableNameTag);

        Element columnsTag = document.createElement("Columns");
        tableTag.appendChild(columnsTag);

        List<ColumnInfo> columns = require.getColumns(tableName);
        
        columns.forEach(colum -> {
            Element column = document.createElement("Column");
            columnsTag.appendChild(column);

            Element columnName = document.createElement("Name");
            columnName.appendChild(document.createTextNode(colum.getName()));
            column.appendChild(columnName);

            Element columnType = document.createElement("DataType");
            columnType.setAttribute("Length", Integer.toString(colum.getColumnSize()));
            columnType.setAttribute("Scale", Integer.toString(colum.getDecimalDigits()));
            columnType.appendChild(document.createTextNode(colum.getDataType()));
            column.appendChild(columnType);

            Element isNull = document.createElement("Nullable");
            isNull.appendChild(document.createTextNode(colum.getIsNullable()));
            column.appendChild(isNull);
        });

        PrimaryKeyInfo pk = require.getPrimaryKey(tableName);
        Element pkTag = document.createElement("PrimayKey");
        tableTag.appendChild(pkTag);
        
        Element pkName = document.createElement("Name");
        pkName.appendChild(document.createTextNode(pk.getName()));
        pkTag.appendChild(pkName);
        
        pk.getColumns().forEach(pkColumn -> {
            Element pkColumnTag = document.createElement("Column");
            pkColumnTag.appendChild(document.createTextNode(pkColumn));
            pkTag.appendChild(pkColumnTag);
        });
        
        writeXml(path, tableName, document);
    }

    private static Document buildXmlDocument() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeXml(Path path, String tableName, Document document) {
        Transformer tf = null;

        try {
            TransformerFactory factory = TransformerFactory
                    .newInstance();
            tf = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }

        tf.setOutputProperty("indent", "yes");
        tf.setOutputProperty("encoding", "UTF-8");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

        try {
            tf.transform(new DOMSource(document), new StreamResult(
                    buildFile(path, tableName)));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private static File buildFile(Path path, String tableName) {
        return new File(path.toString() + "\\" + tableName + ".xml");
    }

    public interface Require {
        List<ColumnInfo> getColumns(String tableName);
        PrimaryKeyInfo getPrimaryKey(String tableName);
    }
}
