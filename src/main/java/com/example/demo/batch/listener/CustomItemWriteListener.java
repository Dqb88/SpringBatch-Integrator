package com.example.demo.batch.listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomItemWriteListener<T> implements ItemWriteListener {

    @Override
    public void beforeWrite(Chunk items) {
        log.info("beforeWrite {}", items);
        ItemWriteListener.super.beforeWrite(items);
    }


    
}
