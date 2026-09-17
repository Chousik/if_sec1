package org.chousik.infosec_1.service;

import org.chousik.infosec_1.dto.CreateDataRequest;
import org.chousik.infosec_1.dto.DataResponse;
import org.chousik.infosec_1.entity.DataItem;
import org.chousik.infosec_1.repository.DataItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DataService {

    private final DataItemRepository dataRepository;

    public DataService(DataItemRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<DataResponse> findAll() {
        return dataRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public DataResponse create(CreateDataRequest request) {
        DataItem savedItem = dataRepository.save(new DataItem(request.content()));
        return toResponse(savedItem);
    }

    public void delete(long id) {
        if (!dataRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data item not found");
        }
        dataRepository.deleteById(id);
    }

    private DataResponse toResponse(DataItem item) {
        return new DataResponse(
                item.getId(),
                HtmlUtils.htmlEscape(item.getContent())
        );
    }
}
