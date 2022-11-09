package com.globits.da.service;

import com.globits.da.support.Response;
import java.util.List;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface BaseService<T> {

     Response<T>create(T dto);

     Response<T> edit(UUID id, T dto);

     Response<T> deleteById(UUID id);

     Response<T> getOne(UUID id);

     Response<List<T>> getAll();
}