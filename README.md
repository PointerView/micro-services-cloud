# Proyecto de microservicios completo con Spring Cloud

## Explicacion detallada del contenido del proyecto

En este proyecto quise crear un sistema basico de manejo de algo parecido a un carrito de compras
implementando el servicio ``product-service`` que se encargaba de manejar los productos como tal y
luego el servicio ``item-service`` que se comunicaba con el ``product-service`` para obtener los productos
necesarios y asi implementar los objetos de item. En este caso como iba a tener codigo redundante por el
hecho de usar clases en iguales en ambos servicios, decidi crear una libreria llamada ``common-service`` que
mas que ser un servicio como tal, era una dependencia que cree para reutilizar estas clases en los servicios
donde se necesitaran.

Toda comunicacion entre los diferentes microservicios se realizo con feing-client y con web-client que aunque 
es reactivo tiene la opcion de hacer uso de forma bloqueante.

Teniendo en mente que los servicios pueden escalar verticalmente y se iba a hacer inmanejable tantas direcciones,
decidi implementar dos servicios que para mi punto de vista son imprescindibles en este tipo de arquitecturas que
son el servidor de nombre ``eureka-service`` para registar todos los servicios y asi hacer uso de sus 'dns' y abstraer
el host y el puerto, y el servidor de recursos ``gateway-service`` para poder aplicar un punto de entrada a todos
los recursos de la aplicacion, ademas permitiendo filtrar accesos a los mismos mediante condiciones y filtros. Ademas
este ultimo servicio gateway es de la dependencia reactiva ya que aunque mi proyecto esta orientado a servlets, a la
hora de una gran cantidad de accesos a la aplicacion seria mas optimo que estas request no sean bloqueantes.

Finalmente quise centralizar toda configuracion de estos microservicios en un ``config-service`` para asi evitar
tener que acceder a los diferentes microservicios si es que se necesita modificar alguna configuracion, de este modo
solo con cambiar las configuraciones que incluí en un repo publico de github se aplicaria en todos los proyectos que
la usen. Para poder aplicar los cambios relizados normalmente se requiere de reiniciar los servicios pero hice uso
del modulo de spring-actuator para poder refrescar el scope de los componentes y asi aplicar las nuevas configuraciones
sin necesidad de reiniciarlos, esto solo se aplicaria a configuraciones que no esten siendo usadas por el propio
servidor como podria ser el puerto o la url de la BBDD
