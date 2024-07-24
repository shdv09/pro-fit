package com.shdv09.webapplication.feign;

import com.shdv09.webapplication.dto.request.ProcessVisitDto;
import com.shdv09.webapplication.dto.response.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "visit-service", url = "localhost:8002")
public interface VisitServiceProxy {

    @PostMapping("/api/visits")
    VisitDto processVisit(@RequestBody ProcessVisitDto dto);
}
