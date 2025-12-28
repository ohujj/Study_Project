package com.exception.exception.global;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReturnDto<T> {

    private String resultCode;
    private String resultMessage;

    private T data;

    public ReturnDto(String resultCode) {
        this.resultCode = resultCode;
        this.resultMessage = null;
        this.data = null;
    }

    public ReturnDto(String resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = null;
    }

    public ReturnDto(String resultCode, String resultMessage, T data) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = data;
    }
}
