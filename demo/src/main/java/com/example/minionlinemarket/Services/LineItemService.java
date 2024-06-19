package com.example.minionlinemarket.Services;

import com.example.minionlinemarket.model.LineItem;

import java.util.List;

public interface LineItemService {
    List<LineItem> findAll();
    LineItem findById(Long id);
    // i need to discuss LineItem

}
