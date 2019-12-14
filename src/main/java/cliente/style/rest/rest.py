import unirest
import json

print ("Introduzca 1 para listar urls, 2 para crear url y 3 para consultar urls publicadas por un usuario")

opcion = raw_input()

if opcion == "1":
    urls = unirest.get("http://localhost:8080/rest/links", headers={ "Accept": "application/json" }).body
    print (urls)

if opcion == "2":
    url = {}
    print ("Digite id de usuario: ")
    id = raw_input()
    print ("Digite la url: ")
    url['url'] = raw_input()

    link = unirest.post("http://localhost:8080/rest/url/crear/"+id, headers={ "Accept": "application/json" }, params=json.dumps(url)).body
    print (link)

if opcion == "3":
    print ("Digite id de usuario: ")
    id = raw_input()
    urls = unirest.get("http://localhost:8080/rest/links/user/"+id, headers={ "Accept": "application/json" }).body

    print (urls)
