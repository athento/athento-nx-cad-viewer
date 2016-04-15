Autogeneración de certificados
==============================

keytool -genkey -keystore Yerbabuena_Athento_OpenDesktopApplicationApplet -alias miguelangelmunoz -validity 3600
Password:YerbabuenaAthento

javac OpenDesktopApplicationApplet.java && jar cvf OpenDesktopApplicationApplet.jar OpenDesktopApplicationApplet.class && jarsigner -keystore Yerbabuena_Athento_OpenDesktopApplicationApplet OpenDesktopApplicationApplet.jar miguelangelmunoz

