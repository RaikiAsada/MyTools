package db.connection.query;

public class SqlUtil {

    /**
     * 8桁の整数で表現された日付データのカラムをdatetime型に変換する式を記述する
     * @param columnName
     * @return
     */
    public static String dateFromDecimal8(String columnName) {
        return String.format(
                "CONVERT(datetime, CONVERT(varchar, %s))",
                columnName);
    }
}
