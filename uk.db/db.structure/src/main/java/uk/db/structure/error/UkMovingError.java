/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.db.structure.error;

import lombok.Getter;

/**
 * 引越し時に発生したエラーのメッセージ
 */
@Getter
public class UkMovingError {
    private String message;

    public UkMovingError(String message) {
        this.message = message;
    }
}
