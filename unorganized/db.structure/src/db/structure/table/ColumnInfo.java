/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure.table;

import lombok.Value;

/**
 * カラム情報
 * @author raiki_asada
 */
@Value
public class ColumnInfo {
    private String name;
    private String dataType;
    private int columnSize;
    private int decimalDigits;
    private String isNullable;
}
