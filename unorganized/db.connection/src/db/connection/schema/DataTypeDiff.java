/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.connection.schema;

/**
 * DBシステムでの型の違いを吸収するためのインタフェース
 */
public interface DataTypeDiff {
    String charType();
    String ncharType();
    
    public class Postgres implements DataTypeDiff {
        @Override
        public String charType() {
            return "bpchar";
        }

        @Override
        public String ncharType() {
            //Postgresはcharがncharを包括している
            return this.charType();
        }
    }
    
    public class SqlServer implements DataTypeDiff {

        @Override
        public String charType() {
            return "char";
        }

        @Override
        public String ncharType() {
            return "nchar";
        }
    }
}
