package com.api.produtosfavoritos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.api.produtosfavoritos")
public class ProdutosFavoritosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProdutosFavoritosApplication.class, args);
	}

}
