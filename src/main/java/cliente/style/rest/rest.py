import unirest
import json

print ("Introduzca 1 para login, 2 para listar urls, 3 para crear url, 4 para consultar urls publicadas por un usuario")

opcion = raw_input()

if opcion == "1":
    user = {}
    print ("Digite el username: ")
    user["username"] = raw_input()
    print ("Digite el password: ")
    user["password"] = raw_input()
    token = unirest.post("http://localhost:8080/rest/login/", headers={ "Accept": "application/json"}, params=json.dumps(user)).body
    print (token)

if opcion == "2":
    print ("Digite la clave: ")
    token = raw_input()
    urls = unirest.get("http://localhost:8080/rest/links", headers={ "Accept": "application/json", "token": token}).body
    print (urls)

if opcion == "3":
    url = {}
    print ("Digite id de usuario: ")
    url['id'] = raw_input()
    print ("Digite la url: ")
    url['url'] = raw_input()
    print ("Digite la clave: ")
    token = raw_input()
    link = unirest.post("http://localhost:8080/rest/url/crear/", headers={ "Accept": "application/json", "token": token}, params=json.dumps(url)).body
    print (link)

if opcion == "4":
    id = {}
    print ("Digite id de usuario: ")
    id['id'] = raw_input()
    urls = unirest.post("http://localhost:8080/rest/links/user/", headers={ "Accept": "application/json", "token": token}, params=json.dumps(id)).body
    print ("Digite la clave: ")
    token = raw_input()
    print (urls)
