package com.main.service.FeignApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "random-org", url = "${FeignApi.url}")
public interface IPorcentajeService {

    @GetMapping("/decimal-fractions/?num=1&dec=2&col=1&format=plain")
    String  obtenerPorcentaje();


    /*
    * Dejo este metodo comentado, pero dejo el ejemplo de como se puede hacer la inserccion de los parametros en la URI
    * Los parametros se pueden decalrar en la eservicio o la clase que los llame:
    *
    *  @Value("${FeignApi.numero}")
    *  private int num;
    *
    * Y luego realizar la inserccion
    *
    *    @GetMapping("/decimal-fractions")
    *    double obtenerPorcentaje(
    *            @RequestParam("num") ("${FeignApi.numero}") int num,
    *            @RequestParam("dec") ("${FeignApi.decimal}") int dec,
    *            @RequestParam("col") ("${FeignApi.col}") int col,
    *            @RequestParam("format") ("${FeignApi.formato}") String format
    *    );
     */
}

