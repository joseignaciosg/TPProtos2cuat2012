*TP 1 - Servidor Proxy para protocolo POP3*

El programa puede compilarse utizando maven2 de la siguiente manera
<pre>
mvn clean package
</pre>

Para correrlo simplemente ejecutar la clase **ProxyInitializer** y levantara del archivo **server.init** los servicios configurados.

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

