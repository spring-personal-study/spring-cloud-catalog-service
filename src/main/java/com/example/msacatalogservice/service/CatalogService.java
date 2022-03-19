package com.example.msacatalogservice.service;

import com.example.msacatalogservice.model.CatalogEntity;
import org.springframework.stereotype.Service;

@Service
public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
