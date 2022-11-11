/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.moving.dom;

import java.util.Objects;
import nts.gul.util.Either;
import uk.db.moving.dom.directory.FileType;
import uk.db.moving.dom.error.UkMovingError;
import uk.db.moving.dom.table.TableInfo;

/**
 * 引越した結果をチェックする
 */
public class CheckMovingData {

    public static Either<UkMovingError, Void> check(Require require, String contractCode, TableInfo table) {
        Either<UkMovingError, Void> exported = ExportDataToCSV.exportTo(require, contractCode, table);

        if (exported.isRight()) {
            return Either.right(exported.getRight());
        }

        Either<UkMovingError, Long> fromReader = require.getFileSize(table.getTable(), FileType.FROM);
        Either<UkMovingError, Long> toReader = require.getFileSize(table.getTable(), FileType.TO);

        return fromReader.mapEither(from -> toReader.map(to -> Objects.equals(from, to))).mapEither(checked -> {
            if (!checked) {
                return Either.left(new UkMovingError("引越し結果の比較失敗　テーブル名： " + table.getTable()));
            }

            return Either.rightVoid();
        });
    }

    public interface Require extends ExportDataToCSV.RequireTo {

        Either<UkMovingError, Long> getFileSize(String tableName, FileType fileType);
    }
}
