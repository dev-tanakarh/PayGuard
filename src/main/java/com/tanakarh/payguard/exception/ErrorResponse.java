package com.tanakarh.payguard.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String code,
    String message,
    Object errors,
    String path
) { }
