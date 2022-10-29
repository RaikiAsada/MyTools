package db.connection.query;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import nts.arc.time.GeneralDate;

@Getter
@AllArgsConstructor
public class UkStatement {

    private final PreparedStatement ps;

    @SneakyThrows
    public void setInt(int parameterIndex, int x) {
        ps.setInt(parameterIndex, x);
    }

    @SneakyThrows
    public void setIntDate(int parameterIndex, GeneralDate x) {
        ps.setInt(parameterIndex, Integer.valueOf(x.toString("yyyyMMdd")));
    }

    @SneakyThrows
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        ps.setBigDecimal(parameterIndex, x);
    }

    @SneakyThrows
    public void setString(int parameterIndex, String x) {
        ps.setString(parameterIndex, x);
    }

    @SneakyThrows
    public void setBoolean(int parameterIndex, boolean x) {
        ps.setBoolean(parameterIndex, x);
    }

    @SneakyThrows
    public void setGeneralDate(int parameterIndex, GeneralDate x) {
        this.setDate(parameterIndex, Date.valueOf(x.toLocalDate()));
    }

    @SneakyThrows
    public void setDouble(int parameterIndex, double x) {
        ps.setDouble(parameterIndex, x);
    }

    @SneakyThrows
    public void setDate(int parameterIndex, Date x) {
        ps.setDate(parameterIndex, x);
    }
}
