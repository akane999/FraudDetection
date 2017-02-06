Argumento de la JVM para que tome la configuracion del netty

-Dtransports.netty.conf=netty-transports.yml

Instalacion del servicio (los archivos en cuestion se encuentran en la raiz del proyecto en eclipse)
1. Crear las siguientes carpetas en el servidor linux
mkdir /usr/java/servicios/contingencia
mkdir /usr/java/servicios/contingencia/log
mkdir /usr/java/servicios/default
mkdir /usr/java/servicios/default/log

2. copiar el archivo netty-transports.yml en la raiz de cada servicio
cp netty-transports.yml /usr/java/servicios/contingencia/netty-transports.yml
cp netty-transports.yml /usr/java/servicios/default/netty-transports.yml

2.5 modificar el puerto del servicio de contingencia, abrir el archivo como texto
/usr/java/servicios/contingencia/netty-transports.yml 
y modificar el puerto a 9002

3. Copiar el jar del servicio en la raiz de cada servicio
cp target/microservice-credibanco-1.0.0-SNAPSHOT.jar /usr/java/servicios/contingencia/microservice-credibanco.jar
cp target/microservice-credibanco-1.0.0-SNAPSHOT.jar /usr/java/servicios/default/microservice-credibanco.jar

4. Copiar los script de servicio en init.d
cp ms-credibanco-contingencia /etc/init.d/ms-credibanco-contingencia
cp ms-credibanco-default /etc/init.d/ms-credibanco-default

5. otorgar permisos de ejecucion a esos archivos
chmod +x /etc/init.d/ms-credibanco-contingencia
chmod +x /etc/init.d/ms-credibanco-default

6. registrar los script de servicio al sistema
chkconfig --add ms-credibanco-contingencia
chkconfig --add ms-credibanco-default

7. registrar a que se inicien en los niveles 3 y 5
chkconfig --level 35 ms-credibanco-contingencia on
chkconfig --level 35 ms-credibanco-default on

8. iniciar los servicios
service ms-credibanco-contingencia start
service ms-credibanco-default start

9. listo
