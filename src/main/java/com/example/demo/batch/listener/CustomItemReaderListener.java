package com.example.demo.batch.listener;

import org.springframework.batch.core.ItemReadListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomItemReaderListener<T> implements ItemReadListener {

    @Override
    public void beforeRead() {
        log.info("beforeRead");
        ItemReadListener.super.beforeRead();
    }
    
}
