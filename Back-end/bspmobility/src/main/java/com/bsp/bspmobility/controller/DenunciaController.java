package com.bsp.bspmobility.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bsp.bspmobility.dto.DenunciaDTO;
import com.bsp.bspmobility.entities.Denuncia;
import com.bsp.bspmobility.service.DenunciaService;

@RestController
@RequestMapping("/denuncias")
@CrossOrigin(origins = "*")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    // Método para Form-data com arquivo
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Denuncia> salvarFormData(
        @RequestParam("prefixo") String prefixo,
        @RequestParam("empresa") String empresa,
        @RequestParam("linha") String linha,
        @RequestParam("placa") String placa,
        @RequestParam("problema") String problema,
        @RequestParam(value = "imagem", required = false) MultipartFile imagem
    ) {
        Denuncia denuncia = new Denuncia();
        denuncia.setPrefixo(prefixo);
        denuncia.setEmpresa(empresa);
        denuncia.setLinha(linha);
        denuncia.setPlaca(placa);
        denuncia.setProblema(problema);

        System.out.println("Denuncia recebida (Form-data): " + denuncia);

        Denuncia denunciaSalva = denunciaService.salvar(denuncia, imagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(denunciaSalva);
    }

    // Método para JSON
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Denuncia> salvarJson(@RequestBody Denuncia denuncia,
        @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        System.out.println("Denuncia recebida (JSON): " + denuncia);

        Denuncia denunciaSalva = denunciaService.salvar(denuncia, imagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(denunciaSalva);
    }

    @GetMapping
    public ResponseEntity<List<DenunciaDTO>> listar() {
        List<DenunciaDTO> result = denunciaService.listar();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/por-linha")
    public ResponseEntity<List<DenunciaDTO>> listarPorLinha(@RequestParam String linha) {
        List<DenunciaDTO> denunciasPorLinha = denunciaService.findByLinha(linha);
        return ResponseEntity.ok(denunciasPorLinha);
    }
}