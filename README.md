# Calculator
Rest Calculator - Java/Spring Boot

- Java 11
- Maven

API para calcular expressões matemáticas que contenham os operadores de adição ('+'), subtração ('-'), multiplicação ('*') e divisão ('/'). Ela retorna o resultado com 2 dígitos de precisão.

Enviar requsição POST para o entry-point '/v1/calculator' com payload no formato JSON:

```
{
	"expressao": "<expr>"
}
```

Para autenticação da chamada, adicionar aos headers o token abaixo

```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJDYWxjdWxhdG9yQXV0aGVudGljYXRpb25Ub2tlbiIsImlhdCI6MTUxNjIzOTAyMn0.VABuaCLS7IJJUa9VsswYjnx1SGVuJJgOFyc8GtFLD8dpkgqqNhdhlR57d68XoeTMoU10ytj9bI2gEO3k-9HlOKWgP37FHpYWG3gDWqO9WBDAXwSJRy7c4jdvXloXTD4kuq3t-AQ6BoASN4iyYPBcr7lkhw4pPVc840vaQ9hnvmqsqf4xogqwKl6wAdj33weaq25Gpfxo7QSaq2yl9lcsIkLZHvIjv2YEDLi2EQVNGbPJzp8gUrCjUmpfw7I6jFHQ0RQkTwQioO7wVtP76zsPjCrjbDNnOYDhMKo1DfNvtcGp8olW8QsLtfGSd6ZR8ZGg4BFzPqdaUHcwJFEFK_ZaEw
```

---

### Gerar próprio token

Para criar seu próprio token você precisará primeiro gerar um par de chaves RSA. Utilize o OpenSSL com os comandos abaixo:

```
   $ openssl genrsa
   $ openssl rsa -in private.pem -pubout -out public.pem
```

Para o Spring Security validar os tokens que você irá gerar com estas chaves. Substitua o arquivo public.pem do projeto pelo arquivo gerado (sua chave publica, public.pem).

Para gerar seu token utilize a página web https://jwt.io/, selecione o algoritmo RS256, na seção Decoded insira suas chaves e edite o token. Pronto, agora é só copiar o token na seção Encoded e utiliza-lo nas requisições.

