package com.salessavvy.product.exception;
import java.time.Instant;
public record ApiError(Instant timestamp, int status, String error) {}
