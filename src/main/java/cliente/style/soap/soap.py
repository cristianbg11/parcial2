
from zeep import Client

url = "http://localhost/ws/urls?wsdl"
client = Client(url)

print ("Introduzca 1 para listar urls, 2 para crear url y 3 para consultar urls publicadas por un usuario")

opcion = raw_input()

if opcion == "1":
    # Listar urls
    urls = client.service.getAllUrls()
    print (urls)

if opcion == "2":
    print ("Digite id de usuario: ")
    id = raw_input()
    print ("Digite la url: ")
    url = raw_input()
    link = client.service.addUrl(url, id)
    print (link)

if opcion == "3":
    print ("Digite id de usuario: ")
    id = raw_input()
    urls = client.service.getUrls(id)
    print (urls)
