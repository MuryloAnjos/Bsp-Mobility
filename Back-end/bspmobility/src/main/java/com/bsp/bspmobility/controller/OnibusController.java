package com.bsp.bspmobility.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsp.bspmobility.model.OnibusInfo;
import com.bsp.bspmobility.model.ParadaLinha;
import com.bsp.bspmobility.service.RotaService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/onibus")
public class OnibusController {

    private final RotaService rotaService;

    public OnibusController(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    @GetMapping("/testar-rota")
    public Flux<OnibusInfo> testarRota(
            @RequestParam String origem,
            @RequestParam String destino) {
        return rotaService.obterInfoOnibusPorRota(origem, destino)
                .doOnNext(info -> System.out.println("Dados obtidos: " + info))
                .doOnError(e -> System.err.println("Erro: " + e.getMessage()));
    }

//    @GetMapping("/testar-parada-linha")
//    public Flux<OnibusInfo> testarParadaELinha(
//            @RequestParam String linha,
//            @RequestParam String parada) {
//        return rotaService.buscarDetalhesOnibus(new ParadaLinha(linha, parada))
//                .flux()
//                .doOnNext(info -> System.out.println("Dados obtidos: " + info))
//                .doOnError(e -> System.err.println("Erro: " + e.getMessage()));
//    }
}