# winter-framework
Framework comme Spring 

## Paramétrage :

    - Dans le web.xml de votre projet , définir le servlet FrontController , avec la classe mg.itu.prom16.controller.FrontController
    - Définir son init-param avec le param-name package_name et avec le param-value qui sera le package ou se trouvera vos controllers.
    - Définir l'urlmapping du FrontController , qui sera /

### Exemple :
    <servlet>
		<servlet-name>FrontController</servlet-name>
		<servlet-class>mg.itu.prom16.controller.FrontController</servlet-class>
		<init-param>
			<param-name>package_name</param-name>
			<param-value>controllers</param-value>
			<description>Package ou se trouve tous les controllers</description>
		</init-param>
	</servlet>
	<servlet-mapping>
		<servlet-name>FrontController</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>

## Guide d'utilisation :

    - Annoter vos Controllers avec @Controller
    - Annoter vos méthodes avec @GetMapping(String url)
    - Pour prendre des données depuis une vue , utilisez @Param sur les parametres de la fonction
    de votre controller ou bien par convention , matchez les params de la requete avec 
    les noms des parametres
    