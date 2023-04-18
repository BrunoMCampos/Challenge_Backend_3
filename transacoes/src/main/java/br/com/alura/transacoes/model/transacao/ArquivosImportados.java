package br.com.alura.transacoes.model.transacao;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ArquivosImportados(List<MultipartFile> arquivos) {
}
