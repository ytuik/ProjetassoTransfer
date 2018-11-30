# ProjetassoTransfer
TRANSFER

1. Objetivo

O objetivo deste miniprojeto é o desenvolvimento de uma aplicação de transferência confiável de arquivos sobre UDP. Para isso, deverá ser implementado na camada aplicação o Selective Repeat e um módulo especial de descarte de mensagens no cliente a fim de avaliar o correto funcionamento da confiabilidade. O programa deverá permitir que o usuário opcionalmente especifique o tamanho da janela do SR na linha de comando.



2. Módulo de descarte

A aplicação cliente deverá ter um módulo especial, como representado na Figura 1, que decide descartar ou não as mensagens retiradas do buffer do socket do lado cliente.  A cada mensagem recebida por tal módulo, um número pseudo-aleatório no intervalo [0, 99] deverá ser escolhido. Caso esse número seja inferior ao percentual configurado pelo usuário na linha de comando, a mensagem deverá ser descartada. Informações sobre geração de números pseudo-aleatórios em Java podem ser obtidas em http://www.devmedia.com.br/numeros-aleatorios-em-java-a-classe-java-util-random/26355

