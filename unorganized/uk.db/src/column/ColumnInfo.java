/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package column;

import db.connection.schema.ColumnName;
import lombok.Value;

/**
 * カラム情報
 * @author raiki_asada
 */
@Value
public class ColumnInfo {
    private ColumnName name;
    private String dataType;
    private Integer columnSize;
    private Integer decimalDigits;
    private String isNullable;
}
