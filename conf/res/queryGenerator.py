import random, hashlib, sys

sys.stdout = open("queries.sql", "w+")

print "--- Arquivo de inicializacao"
print "--- Rode apenas quando quiser resetar o banco de dados"
print "TRUNCATE TABLE disciplina CASCADE;"
print "TRUNCATE TABLE periodo CASCADE;"
print "TRUNCATE TABLE grade CASCADE;"
print "ALTER SEQUENCE disciplina_seq RESTART WITH 1;"
print "ALTER SEQUENCE periodo_seq RESTART WITH 1;"
print "ALTER SEQUENCE grade_seq RESTART WITH 1;"
print "ALTER SEQUENCE usuario_seq RESTART WITH 1;"

with open("usuarios.txt", "r+") as f:
	for idx, line in enumerate(f.readlines()):
		parts = line.strip().split(":")
		if len(parts) < 3: continue
		id = idx + 1
		name, username, password = parts[0], parts[1].split("@")[0], parts[2]
		salt = str(random.randint(1000000000, 9999999999))
		hashedPassword = hashlib.md5(password+salt).hexdigest()
		print "INSERT INTO usuario (id, grade_id, nome, usuario, senha_hasheada, salt) VALUES ('%d', '%d', '%s', '%s', '%s', '%s');" % (id, id, name, username, hashedPassword, salt)
		print "INSERT INTO grade (id, periodo_cursando) VALUES ('%d', '%d');" % (id, 1)