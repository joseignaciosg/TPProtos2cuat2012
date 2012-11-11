*TP 1 - Servidor Proxy para protocolo POP3*

El programa puede compilarse utizando maven2 de la siguiente manera
<pre>
mvn clean package
</pre>

Luego para correrlo simplemente ejecutar el jar de la siguiente forma: 

<pre>
java -jar proxy_file.jar PROXY_CONFIG_FILES_PATH DEFAULT_ORIGIN_SERVICE PORT
</pre>

Ejemplo:

> java -jar proxy-pop3-1.jar ./myresources/ mysuperserver.no-ip.org 110

Siendo **"./myresources"** el path al directorio con los archivos de configuración el cual tiene la siguiente estructura:

<pre>
├── myresources
│   ├── conf
│   │   ├── access_amount.conf
│   │   ├── access_time.conf
│   │   ├── banned_ip.conf
│   │   ├── configurer.conf
│   │   ├── external_transformation.conf
│   │   ├── log4j.properties
│   │   ├── notdelete_content_type.conf
│   │   ├── notdelete_header_pattern.conf
│   │   ├── notdelete_max_age.conf
│   │   ├── notdelete_sender.conf
│   │   ├── notdelete_size.conf
│   │   ├── notdelete_structure.conf
│   │   ├── origin_server.conf
│   │   ├── stats_service.conf
│   │   └── transformation.conf
│   ├── mime
│   │   └── contenttype
│   ├── project.properties
│   └── server.init
</pre>


-------------------------

*Archivos de configuración:*

server.init
> FullServiceClassName, portNumber

access_amount.conf
> nombreDeUsuario = cantidadMaximaDeLoginsPorDia

access_time.conf
> nombreDeUsuario = hh:mm-hh:mm (desde-hasta)

banned_ip
> direccionIp

configurer.conf
> password = passwordParaAcceder

> (otros)

external_transformations.conf
> programaYArgumentosAEjecutar

notdelete_content_type.conf
> nombreDeUsuario = extensionesBloqueantes

notdelete_header_pattern.conf
> nombreDeUsuario = patronesEnLosHeadersBloqueanes

notdelete_max_age.conf
> nombreDeUsuario = dd/mm/yyyy

notdelete_sender.conf
> nombreDeUsuario = listadoDeUsuarios

notdelete_size.conf
> nombreDeUsuario = tamañoMaximoPermitido

notdelete_structure.conf
> nombreDeUsuario = dissabledAttachments

origin_server.conf
> nombreDeUsuario = hostDelOriginServer

> default = hostDelOriginServer

stats_service.conf.conf
> password = passwordParaAcceder

> (otros)

transformation.conf.conf
> hidesender

> l33t

> rotateimages

