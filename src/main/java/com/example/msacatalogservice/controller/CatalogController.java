package com.example.msacatalogservice.controller;

import com.example.msacatalogservice.model.CatalogDto;
import com.example.msacatalogservice.model.CatalogEntity;
import com.example.msacatalogservice.model.ResponseCatalog;
import com.example.msacatalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working in User Service on Port %s", request.getServerPort());
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogs = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();

        catalogs.forEach(v -> {
            ResponseCatalog responseCatalog = new ResponseCatalog();
            BeanUtils.copyProperties(v, responseCatalog);
            result.add(responseCatalog);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
