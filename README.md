# winter-framework
Framework comme Spring 

## Paramétrage :

    - Dans le web.xml de votre projet , définir le servlet FrontController , avec la classe mg.itu.prom16.controller.FrontController
    - Définir son init-param avec le param-name package_name et avec le param-value qui sera le package ou se trouvera vos controllers.
    - Définir l'urlmapping du FrontController , qui sera /

## Guide d'utilisation :

    - Annoter vos Controllers avec @Controller
    - Annoter vos méthodes avec @GetMapping(String url)
    