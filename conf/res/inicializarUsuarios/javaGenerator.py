import random, hashlib

## Le o codigo java a ser usado como base
with open("InicializadorDeUsuarios.java", "r+") as f:
	code = "".join(f.readlines())
tab_size = int(code.split("//%RegisterCode(")[1].split(")%//")[0])

## Obtem a lista de usuarios (usuarios.txt)
users = []
with open("usuarios.txt", "r+") as f:
	for idx, line in enumerate(f.readlines()):
		parts = line.strip().split(":")
		if len(parts) < 3: continue
		id = idx + 1
		name, username, password = parts[0], parts[1].split("@")[0], parts[2]
		users.append((name, username, password))

## Gera o codigo com a lista de usuarios
content = ("if (!cadastroDeUsuario.existeUsuario(\"%s\")) {" % users[0][1]) + "\n"
tabs = "\t" * (tab_size + 1)
content += "\n".join(["%sregistrar(\"%s\", \"%s\", \"%s\");" % (tabs, n, u, p) for (n, u, p) in users])
content += "\n" + ("\t" * tab_size) + "}"

## Procura o diretorio raiz do projeto
root_path = ""
while not os.path.isdir(root_path + "app/controllers/"):
	root_path += "../"

## Escreve o codigo final no arquivo java
code = code.replace("//%%RegisterCode(%d)%%//" % tab_size, content)
with open(root_path + "app/controllers/InicializadorDeUsuarios.java", "w+") as f:
	f.write(code)
