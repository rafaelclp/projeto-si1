import random, hashlib

#print "Usuario u = null;"
#print "try {"
with open("usuarios.txt", "r+") as f:
	for idx, line in enumerate(f.readlines()):
		parts = line.strip().split(":")
		if len(parts) < 3: continue
		id = idx + 1
		name, username, password = parts[0], parts[1].split("@")[0], parts[2]
		print "Control.registrar(\"%s\", \"%s\", \"%s\");" % (name, username, password)
		#print "u.getGrade().resetar();"
#print "} catch (InvalidOperationException e) {"
#print "e.printStackTrace();"
#print "}"