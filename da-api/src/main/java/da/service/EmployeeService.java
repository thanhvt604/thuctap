package com.globits.da.service;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.support.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
public interface EmployeeService extends BaseService<EmployeeDto> {
    Response<List<EmployeeDto>> exportToExcel(HttpServletResponse response);

    Response<List<String>> importExcel(MultipartFile multipartFile);
}
