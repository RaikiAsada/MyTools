/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure.table;

import java.util.List;
import lombok.Value;

@Value
public class PrimaryKeyInfo {
    private String name;
    private List<String> columns;
}
