package com.kiro.s3.compatible.filesystem;

/**
 * This interface provides api for each filesystem operation
 *
 * @author christian
 */
public interface FileSystemOperation<T> {
    /**
     * This method should contain the main logic of specific filesystem operation implementation
     */
    void execute();

    /**
     * This method should return the result of executed filesystem operation
     */
    T getResult();
}
