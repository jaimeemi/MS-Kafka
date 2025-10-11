package com.challengeTenpo.service;

import com.challengeTenpo.exceptions.BaseDatosException;
import com.challengeTenpo.exceptions.CalculoDinamicoException;
import com.challengeTenpo.exceptions.FeignApiException;
import com.challengeTenpo.models.Request.CalculoDinamicoRequest;
import com.challengeTenpo.models.Response.CalculoDinamicoResponse;
import com.challengeTenpo.models.Response.HistorialCalculosResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICalculosService {

    CalculoDinamicoResponse calculoDinamico(CalculoDinamicoRequest request, String url) throws CalculoDinamicoException, FeignApiException, BaseDatosException;

    List<HistorialCalculosResponse> historial();

}
