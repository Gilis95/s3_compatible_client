package com.kiro.s3.compatible.filesystem;

public interface FileSystemOperation<T> {
    void execute();

    T getResult();
}
