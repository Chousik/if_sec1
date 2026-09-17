package org.chousik.infosec_1.controller;

import jakarta.validation.Valid;
import org.chousik.infosec_1.dto.CreateDataRequest;
import org.chousik.infosec_1.dto.DataResponse;
import org.chousik.infosec_1.service.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public List<DataResponse> getData() {
        return dataService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse addData(@Valid @RequestBody CreateDataRequest request) {
        return dataService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteData(@PathVariable long id) {
        dataService.delete(id);
    }
}
