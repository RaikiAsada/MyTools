package db.connection.query;

import db.connection.DBType;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

@AllArgsConstructor
public class UkResultSet {
    private DBType dbTpye;
    private ResultSet rs;

    public List<Object> getObjects(List<String> names) {
        return names.stream()
                .map(n -> getObject(n))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Object getObject(String name) {
        return rs.getObject(dbTpye.convert(name));
    }

    @SneakyThrows
    public String getString(String name) {
        return rs.getString(dbTpye.convert(name));
    }

    @SneakyThrows
    public Integer getInt(String name) {
        int value = rs.getInt(dbTpye.convert(name));
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Long getLong(String name) {
        long value = rs.getLong(dbTpye.convert(name));
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Boolean getBoolean(String name) {
        boolean value = rs.getBoolean(dbTpye.convert(name));
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Date getDate(String name) {
        return rs.getDate(dbTpye.convert(name));
    }

    @SneakyThrows
    public Double getDouble(String name) {
        return rs.getDouble(dbTpye.convert(name));
    }

    @SneakyThrows
    public BigDecimal getBigDecimal(String name) {
        return rs.getBigDecimal(dbTpye.convert(name));
    }

    @SneakyThrows
    public GeneralDate getGeneralDate(String name) {
        Date value = rs.getDate(dbTpye.convert(name));
        return value == null ? null : GeneralDate.localDate(value.toLocalDate());
    }

    @SneakyThrows
    public GeneralDateTime getGeneralDateTime(String name) {
        Timestamp value = rs.getTimestamp(dbTpye.convert(name));
        return value == null ? null : GeneralDateTime.localDateTime(value.toLocalDateTime());
    }

    @SneakyThrows
    public <E extends Enum<?>> E getEnum(String name, Class<E> enumClass) {
        Integer value = this.getInt(name);
        return value == null ? null : EnumAdaptor.valueOf(value, enumClass);
    }

    @SneakyThrows
    public Object getObject(int idx) {
        return rs.getObject(idx);
    }

    @SneakyThrows
    public String getString(int idx) {
        return rs.getString(idx);
    }

    @SneakyThrows
    public Integer getInt(int idx) {
        int value = rs.getInt(idx);
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Long getLong(int idx) {
        long value = rs.getLong(idx);
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Boolean getBoolean(int idx) {
        boolean value = rs.getBoolean(idx);
        return rs.wasNull() ? null : value;
    }

    @SneakyThrows
    public Date getDate(int idx) {
        return rs.getDate(idx);
    }

    @SneakyThrows
    public Double getDouble(int idx) {
        return rs.getDouble(idx);
    }

    @SneakyThrows
    public BigDecimal getBigDecimal(int idx) {
        return rs.getBigDecimal(idx);
    }

    @SneakyThrows
    public GeneralDate getGeneralDate(int idx) {
        Date value = rs.getDate(idx);
        return value == null ? null : GeneralDate.localDate(value.toLocalDate());
    }

    @SneakyThrows
    public GeneralDateTime getGeneralDateTime(int idx) {
        Timestamp value = rs.getTimestamp(idx);
        return value == null ? null : GeneralDateTime.localDateTime(value.toLocalDateTime());
    }

    @SneakyThrows
    public <E extends Enum<?>> E getEnum(int idx, Class<E> enumClass) {
        Integer value = this.getInt(idx);
        return value == null ? null : EnumAdaptor.valueOf(value, enumClass);
    }

    @SneakyThrows
    public boolean next() {
        return rs.next();
    }

    @SneakyThrows
    public boolean isAfterLast() {
        return rs.isAfterLast();
    }

    @SneakyThrows
    public void close() {
        rs.close();
    }
}
