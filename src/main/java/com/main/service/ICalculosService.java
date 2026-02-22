package com.main.service;

import com.main.exceptions.BaseDatosException;
import com.main.exceptions.CalculoDinamicoException;
import com.main.exceptions.FeignApiException;
import com.main.models.Request.CalculoDinamicoRequest;
import com.main.models.Response.CalculoDinamicoResponse;
import com.main.models.Response.HistorialCalculosResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICalculosService {

    CalculoDinamicoResponse calculoDinamico(CalculoDinamicoRequest request, String url) throws CalculoDinamicoException, FeignApiException, BaseDatosException;

    List<HistorialCalculosResponse> historial();

}
