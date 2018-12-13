# ProjetassoTransfer
TRANSFER

1. Objetivo

O objetivo deste miniprojeto é o desenvolvimento de uma aplicação de transferência confiável de arquivos sobre UDP. Para isso, deverá ser implementado na camada aplicação o Selective Repeat e um módulo especial de descarte de mensagens no cliente a fim de avaliar o correto funcionamento da confiabilidade. O programa deverá permitir que o usuário opcionalmente especifique o tamanho da janela do SR na linha de comando.



2. Módulo de descarte

A aplicação cliente deverá ter um módulo especial, como representado na Figura 1, que decide descartar ou não as mensagens retiradas do buffer do socket do lado cliente.  A cada mensagem recebida por tal módulo, um número pseudo-aleatório no intervalo [0, 99] deverá ser escolhido. Caso esse número seja inferior ao percentual configurado pelo usuário na linha de comando, a mensagem deverá ser descartada. Informações sobre geração de números pseudo-aleatórios em Java podem ser obtidas em http://www.devmedia.com.br/numeros-aleatorios-em-java-a-classe-java-util-random/26355


3. Tutorial do ProjetassoTransfer

Será enviado algum arquivo do Servidor para o Cliente;
1 - Abra o arquivo "ServidorSR.java" e coloque o IP do cliente nele; (linha 39)
2 - Abra o arquivo "Cliente.java" e coloque o IP do servidor nele; (linha 49 e 58)
3 - Ainda no "Cliente.java" coloque o destino pra onde o arquivo irá ser transferido, (o ideal é só mudar o nome de usuário); (linha 55 e 63)
4 - No "Servidor.java" coloque o caminho do arquivo a ser enviado (linha 29);
5 - Execute o "Servidor.java" e o "Cliente.java";
6 - No console do "Cliente.java" escolha se irá querer colocar o tamanho da janela, caso escolha não, o default será tamanho 10;(linha 15 e 28)
7 - Em seguida coloque um valor de 0 a 99 para informar a porcentagem de perdas de pacotes, em caso de 0, ele irá perder nenhum pacote, em caso de 99, 99% dos dados serão perdidos;(linha 34)
8 - Coloque o nome do arquivo que será criado no destino informado no código;(linha 43)
9 - Aguarde o arquivo ser enviado. Boa noite! e tenha um Feliz Natal!!!(linha 66)
