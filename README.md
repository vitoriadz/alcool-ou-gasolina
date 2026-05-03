# Álcool ou Gasolina versão Estendida

Este projeto é uma evolução do aplicativo "Álcool ou Gasolina", desenvolvido para a disciplina de Programação para Dispositivos Móveis sob orientação do Prof. Windson Viana. 

O objetivo do app é auxiliar o usuário a calcular o combustível mais vantajoso, permitindo salvar um histórico de postos com localização e data.

## Equipe
* Maria Vitória Diniz de Oliveira
* Sabrina da Silva Lopes

## Demonstração (Vídeo)

Assista ao vídeo demonstrativo das funcionalidades:

👉 **[Link para o Vídeo de Execução](https://drive.google.com/file/d/1Miyy0iURFxhF1KVo1zPWbd4dys-57uGo/view?usp=sharing)**

## Requisitos Implementados

### 1. Persistência de Preferências
* O estado do switch (preferência Álcool ou Gasolina) é salvo via `SharedPreferences`. Ao fechar e abrir o app, a escolha do usuário permanece ativa.

### 2. CRUD Completo com SharedPreferences
* **Cadastro:** Salva nome do posto, valores dos combustíveis e localização.
* **Listagem:** Exibição de postos de forma organizada.
* **Edição/Exclusão:** Possibilidade de atualizar preços ou remover postos da lista.
* **Persistência:** Uso de serialização manual com org.json para converter objetos complexos em Strings.

### 3. Localização e Mapas
* **Permissões:** O app solicita permissão de `ACCESS_FINE_LOCATION` em tempo de execução.
* **Integração:** Ao selecionar um posto, é possível abrir a localização exata através de uma **Intent** para o Google Maps.

### 4. Internacionalização 
* O aplicativo oferece suporte total a dois idiomas:
  * 🇧🇷 **Português**
  * 🇺🇸 **Inglês**
* A tradução é automática com base no idioma do sistema operacional.

## 🚀 Como testar o projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/vitoriadz/alcool-ou-gasolina.git
